package com.mon.fpc.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.mon.fpc.mapper.ChatCacheDao;
import com.mon.fpc.vo.ChatMessageVo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author clic
 * @date 2023-06-10 14:31
 * @description:
 */

@Component
//前端连接socket的时候传递当前用户的ID
//因为我使用的是 springcloud-gateway 的结构，通过 gateway 连接 websocket 总是失败，网上的方法均为生效
//在这里暂时跳过了 gateway ，但是由于其他限制，跳过 gateway 导致获取不到用户信息，所以这里用户 id 由前端传递
//正常来说通过后台自己获取当前用户的信息
@ServerEndpoint(value = "/websocket/{senderId}")
@Log4j2
@EnableScheduling
public class ChatOnlineWebSocket {
    //保存聊天记录的Dao
    private static ChatCacheDao chatCacheDao;

    @Autowired
    public void setChatCacheDao(ChatCacheDao chatCacheDao) {
        this.chatCacheDao = chatCacheDao;
    }

    //这个session是当前websocket的session
    private Session session;

    //记录当前的用户ID
    private String currentId = "";

    //存放在线的用户数量
    private static AtomicInteger userNumber = new AtomicInteger(0);

    //锁 主要用户聊天记录定时存储时锁住messageList 避免clear时导致部分信息丢失
    //存储消息的逻辑使用了简单的循环 每隔10秒缓存一次 或者消息数量大于100缓存一次
    final transient Object lock = new Object();
    private static List<ChatMessageVo> messageList = new ArrayList<>();

    //存放websocket的集合
    private static CopyOnWriteArraySet<ChatOnlineWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    private void uploadCache() {
        List<ChatMessageVo> insertData = new ArrayList<>();
        synchronized (lock) {
            for (ChatMessageVo chatMessageVo : messageList) {
                insertData.add(chatMessageVo.clone());
            }
            messageList.clear();
        }
        //批量插入
        if (insertData.size() > 0) {
            chatCacheDao.batchInsert(insertData);
        }
    }

    @Bean
    private void uploadData() {
        AtomicReference<LocalDateTime> datetime = new AtomicReference<>(LocalDateTime.now());
        new Thread(() -> {
            while (true) {
                if (messageList.size() > 100) {
                    //消息超过100缓存
                    uploadCache();
                    //刷新缓存时间
                    datetime.set(LocalDateTime.now());
                } else {
                    Duration duration = Duration.between(datetime.get(), LocalDateTime.now());
                    long count = duration.toMillis();
                    if (count >= 10000) {
                        //消息数量没超过100 每十秒缓存一次
                        uploadCache();
                        //刷新缓存时间
                        datetime.set(LocalDateTime.now());
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception ex) {
                    log.error("TimeUnit.SECONDS.sleep ERROR");
                }
            }
        }).start();
    }

    //每五秒向前端发送一次数据 避免前端自动断开连接
    //前端在发送消息失败之后也可以先尝试重新连接
    @Scheduled(cron = "0/5 * * * * ?")
    private void configureTasks() throws Exception {
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendText("链接成功");
        }
    }

    //前端请求时一个websocket时,从登陆信息中获取用户信息
    @OnOpen
    public void onOpen(Session session, @PathParam("senderId") String userId) throws IOException {

        //保留当前session
        this.session = session;

        //保存当前用户信息
        currentId = userId;

        //获取已经登陆的用户信息 并 判断当前用户是不是已经在别的地方登陆过了
        //用于用户登录后去更新其他用户的在线状态
        Set<String> userLists = new TreeSet<>();
        boolean hasLogin = false;
        for (ChatOnlineWebSocket webSocket : webSocketSet) {
            if (webSocket.currentId.equals(this.currentId)) {
                //已经存在当前用户ID 表示已经登陆过了 需要通知之前上线的机器下线
                ChatMessageVo chatMessageVo = new ChatMessageVo();
                chatMessageVo.setMessageType(4);
                chatMessageVo.setSenderId(this.currentId);
                chatMessageVo.setMessage("异地登陆");
                //先给旧的登录发送异地登陆信息
                sendMessageTo(com.alibaba.fastjson.JSON.toJSONString(chatMessageVo), this.currentId);
                //在线用户列表添加用户ID
                userLists.add(webSocket.currentId);
            } else {
                //在线用户列表添加用户ID
                userLists.add(webSocket.currentId);
            }
        }
        if (!hasLogin) {
            //增加在线人数
            userNumber.addAndGet(1);
            //在线列表中添加当前用户ID
            userLists.add(this.currentId);
        }
        //当前对象放入webSocketSet
        webSocketSet.add(this);
        //将所有信息包装好将上线信息发给所有用户，让他们更新自己的用户列表
        ChatMessageVo chatMessageVo = new ChatMessageVo();
        chatMessageVo.setUserLists(userLists);
        chatMessageVo.setMessageType(1);
        chatMessageVo.setSenderId(this.currentId);
        chatMessageVo.setMessage("用户上线");
        chatMessageVo.setNumber(this.userNumber.get());
        sendMessageAll(com.alibaba.fastjson.JSON.toJSONString(chatMessageVo));
        log.info("【websocket消息】有新的连接, 总数:{}", this.userNumber);
    }

    //前端关闭时一个websocket时
    @OnClose
    public void onClose() throws IOException {
        //从集合中移除当前对象
        webSocketSet.remove(this);
        //在线用户数减少
        userNumber.addAndGet(-1);

        //将所有信息包装好传到客户端(给所有用户)
        ChatMessageVo chatMessageVo = new ChatMessageVo();
        chatMessageVo.setMessageType(2);
        chatMessageVo.setMessage("用户下线");
        chatMessageVo.setSenderId(this.currentId);
        chatMessageVo.setNumber(this.userNumber.get());
        sendMessageAll(com.alibaba.fastjson.JSON.toJSONString(chatMessageVo));
        log.info("【websocket消息】连接断开, 总数:{}", webSocketSet.size());
    }

    //前端向后端发送消息
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
        //将前端传来的数据进行转型
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(message); //JSON.parseObject(message);
        //消息文本
        String textMessage = jsonObject.getString("message");
        //消息uuid
        String uuid = jsonObject.getString("uuid");

        //判断是不是私发
        String receiverId = null;
        try {
            receiverId = jsonObject.getString("receiverId");
        } finally {
            if (StringUtils.isEmpty(receiverId)) {
                throw new RuntimeException("消息缺少接收对象！");
            }
        }

        ChatMessageVo chatMessageVo = new ChatMessageVo();
        chatMessageVo.setMessageType(3);
        chatMessageVo.setSenderId(this.currentId);
        chatMessageVo.setReceiverId(receiverId);
        chatMessageVo.setMessage(textMessage);
        chatMessageVo.setUuid(uuid);

        if (!receiverId.startsWith("group_")) {
            //私发
            chatMessageVo.setReceiverId(receiverId);
            //发给指定人
            sendMessageTo(com.alibaba.fastjson.JSON.toJSONString(chatMessageVo), receiverId);
            //给自己也得发送一份 主要用于告知自己消息已经发送成功
            sendMessageTo(com.alibaba.fastjson.JSON.toJSONString(chatMessageVo), this.currentId);
        } else {
            //群发
            //目前就一个群
            chatMessageVo.setReceiverId(receiverId);
            sendMessageAll(com.alibaba.fastjson.JSON.toJSONString(chatMessageVo));
        }

        //先缓存记录消息
        synchronized (lock) {
            messageList.add(chatMessageVo);
        }
    }

    /**
     * 消息发送所有人
     */
    public void sendMessageAll(String message) throws IOException {
        for (ChatOnlineWebSocket webSocket : webSocketSet) {
            //消息发送所有人（同步）getAsyncRemote
            webSocket.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 消息发送指定人
     */
    public void sendMessageTo(String message, String toUserId) throws IOException {
        //遍历所有用户
        for (ChatOnlineWebSocket webSocket : webSocketSet) {
            if (webSocket.currentId.equals(toUserId)) {
                //消息发送指定人
                webSocket.session.getBasicRemote().sendText(message);
                log.info("【发送消息】:", this.currentId + "向" + webSocket.currentId + "发送消息：" + message);
                break;
            }
        }
    }
}
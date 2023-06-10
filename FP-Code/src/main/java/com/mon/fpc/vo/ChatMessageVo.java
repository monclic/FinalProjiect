package com.mon.fpc.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author clic
 * @date 2023-06-10 14:25
 * @description:
 */

@Data
public class ChatMessageVo implements Serializable,Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     * 1 用户上线
     * 2 用户下线
     * 3 普通消息
     * 4 异地登陆
     */
    private Integer messageType;
    /**
     * uuid 消息ID
     */
    private String uuid;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 在线用户列表
     */
    private Set<String> userLists;

    /**
     * 在线人数
     */
    private Integer number;

    /**
     * 消息发送人
     */
    private String senderId;

    /**
     * 消息接收人
     */
    private String receiverId;

    /**
     * 内容类型
     * 1 普通文本
     * 2 图片
     * 3 视频
     */
    private Integer contentType;

    /**
     * 发送时间
     */
    private Date sendTime;

    @Override
    public ChatMessageVo clone() {
        try {
            return (ChatMessageVo)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.getCause());
        }
    }


}


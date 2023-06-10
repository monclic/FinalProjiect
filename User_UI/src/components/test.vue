<template>
  <div style="width: 100%; height: 100vh">
    <div class="chat_horline"> </div>
    <div class="content">
      <div class="content__verline"></div>
      <div class="content__left">
        <!-- 分为聊天室和私聊窗口 -->
        <!-- 聊天室按钮 -->
        <div class="content__left_chatroom">
          <div
            v-for="(value, key) in filterUserList('room', '')"
            :key="key"
            :class="key == currentTalkUserId ? 'content__left_item_selected' : 'content__left_item'"
            @click.stop="clickCard(key)"
            ><Badge :count="value.unread" :offset="[22, 0]">
              <div> {{ value.realname }}</div>
            </Badge>

            <div style="font-weight: 500"> 用户A：我发了个消息 </div>
          </div>
        </div>
        <!-- 成员列表 -->
        <div class="content__left_user_searcher">
          <a-input-search v-model:value="searchValue" placeholder="搜索联系人" @search="onSearch"
        /></div>
        <!-- <p>用户列表</p> -->
        <div class="content__left_user">
          <div
            v-for="(value, key) in filterUserList('user', searchValue)"
            :key="key"
            :class="key == currentTalkUserId ? 'content__left_item_selected' : 'content__left_item'"
            @click.stop="clickCard(key)"
          >
            <Badge :count="value.unread" :offset="[0, 0]">
              <div>
                <img style="float: left; height: 35px; width: 40px; padding-left: 5px" />
              </div>
            </Badge>
            <div style="top: 10px; display: inline-block; margin-left: 15px">
              {{ value.realname }}
              <div v-if="value.login"><Badge style="color: green" color="green" text="在线" /></div>
              <div v-else><Badge style="color: gray" color="gray" text="离线" /></div>
            </div>
          </div>
        </div>
      </div>
      <div class="content__verline"></div>
      <div class="content__right">
        <div class="content__right_room">
          <!-- 聊天框 -->
          <div class="chat_content_parent">
            <div ref="messageBoxRef" class="chat_content_child">
              <div
                style="margin-bottom: 0px"
                v-for="(value, key, index) in messageList"
                :key="index"
              >
                <div v-html="showMessage(value)"></div>
              </div>
            </div>
          </div>
          <!-- 分割线 -->
          <div class="chat_line"> </div>
          <!-- 输入框 + 发送按钮 -->
          <div class="chat_sender">
            <div class="sender_header">
              <Popover v-model:visible="popoverVisible" trigger="click" placement="topLeft">
                <template #content>
                  <div style="height: 260px; overflow: auto">
                    <!-- 表情列表 -->
                    <a-card title="" style="width: 337px; height: 100%">
                      <a-card-grid
                        v-for="(value, name, index) in emojisList"
                        :key="index"
                        style="width: 30px; height: 30px; text-align: center; padding: 0px"
                      >
                        <img :src="value" @click="clickEmo(name)" />
                      </a-card-grid>
                    </a-card>
                  </div>
                </template>
                <SmileOutlined class="icon_left" />
              </Popover>
              <Popover trigger="click" placement="topLeft">
                <template #content> </template>
                <PlusCircleOutlined class="icon_left" />
              </Popover>
              <Button class="btn_right" size="small" @click="clickSend"> 发送</Button>
            </div>
            <a-textarea ref="textareaRef" v-model:value="messageText" :bordered="false" />
          </div>
        </div>
      </div>
      <div class="content__verline"></div>
      <div class="content__components"></div>
      <div class="content__verline"></div>
    </div>
    <div class="chat_horline"> </div>
  </div>
</template>
<script lang="ts">
  import { PlusCircleOutlined, SmileOutlined } from '@ant-design/icons-vue';
  import { Button, Popover, Badge } from 'ant-design-vue';
  import { defineComponent, ref } from 'vue';
  import { errorImg, loadingImg } from '../utils/chat.data';
  // import { useMessage } from '/@/hooks/web/useMessage';
  // import { Persistent } from '/@/utils/cache/persistent';
  // import { getUserListApi, getMessListApi } from '/@/api/business/chatonline';
  // import { getUserInfo } from '/@/api/sys/user';

  export default defineComponent({
    components: { Button, Popover, Badge, SmileOutlined, PlusCircleOutlined },
    setup() {
      //表情列表
      const emojisList = ref({});
      //当前用户
      const currentUser = ref('');
      //其他用户列表
      //{
      //  realname: 真实姓名,
      //  login: 登陆状态,
      //  type: 类型 用户/群组,
      //  unread:  未读消息,
      //};
      const usersList = ref({});
      //当前聊天的对象或者是群组的ID
      const currentTalkUserId = ref(null);
      //准备发送的消息
      const messageText = ref('');
      //查询用户条件
      const searchValue = ref('');
      //表情popover是否显示
      const popoverVisible = ref(false);
      //输入框组件
      const textareaRef = ref(null);
      //消息展示框
      const messageBoxRef = ref(null);
      //消息列表
      // {
      //   id: {
      //     isSelf: false, 是否是自己发送的消息
      //      message: data.message, 消息
      //      senderId: data.senderId,发送人ID
      //      receiverId: data.receiverId,接收人ID
      //      loading: true,发送中？
      //      success: false,发送成功？
      //   }
      // }
      const messageList = ref({});

      // const { notification, createErrorModal } = useMessage();
      //定义一个websocket
      var websocket :any= null;
      // 初始化用户列表
      const initUserList = async () => {

        //先加上一个聊天室这是固定的
        usersList.value['group_181eaae57ee84b1296efa45bd1d86801'] = {
          realname: '聊天室',
          login: true,
          type: 'room',
          unread: 0,
        };
        //调用接口查询用户列表
        const users = await getUserListApi();
        if (users !== undefined && users.length > 0) {
          for (let i = 0; i < users.length; i++) {
            usersList.value[users[i].id] = {
              realname: users[i].realName,
              //默认登陆状态都是未登录
              login: false,
              type: 'user',
              unread: 0,
            };
          }
        }

        //默认选中聊天室
        clickCard('group_181eaae57ee84b1296efa45bd1d86801');
      };
      //初始化WebSocket
      const wsInit = async () => {
        if (websocket) {
          return;
        }
        //获取当前用户的信息
        currentUser.value = await getUserInfo();
        //判断当前浏览器是否支持WebSocket（固定写法）
        if ('WebSocket' in window) {
          websocket = new WebSocket('ws://localhost:16001/websocket/' + currentUser.value.userId);
        } else {
          // notification.error({
          //   description: '浏览器不支持websocket',
          //   duration: 3,
          // });
        }

        //连接发生错误的回调方法
        websocket.onerror = function (e) {
          console.log('发生错误', e);
        };

        //连接成功建立的回调方法
        websocket.onopen = function () {
          console.log('建立连接' + 'event');
        };

        //接收到消息的回调方法
        websocket.onmessage = function (event:any) {
          var data = JSON.parse(event.data);
          if (data.messageType === 3) {
            const uuid = data.uuid;
            console.log(uuid, messageList.value[uuid]);
            if (messageList.value[uuid] && messageList.value[uuid].isSelf == true) {
              //当前聊天信息列表中在该条消息 并且发回到了自己 表示消息发送成功
              messageList.value[uuid].loading = false;
              messageList.value[uuid].success = true;
            } else {
              //用户聊天消息
              //判断接受人是群组还是自己
              //是自己表示是有人单独给自己发消息
              let id = '';
              if (data.receiverId.indexOf('group_') > -1) {
                id = data.receiverId;
              } else {
                id = data.senderId;
              }
              //判断当前聊天窗口的用户/群组ID 是不是发送消息的用户ID 或 群组ID
              if (id === currentTalkUserId.value) {
                const uuid = guid();
                messageList.value[uuid] = {
                  isSelf: false,
                  message: data.message,
                  senderId: data.senderId,
                  receiverId: data.receiverId,
                  loading: false,
                  success: true,
                };
              } else {
                //如果不是的话 就在卡片上显示一个有消息的框
                usersList.value[id].unread = usersList.value[id].unread + 1;
              }
            }
          } else if (data.messageType === 4) {
            //弹窗提示 并直接关闭聊天窗口
            // createErrorModal({
            //   title: '警告',
            //   content: '用户异地登录聊天室，当前连接已断开',
            //   getContainer: () => document.body,
            // });
            //断开连接
            closeSocket();
          } else if (data.messageType === 1 || data.messageType === 2) {
            const logined = data.userLists;
            //等一会再处理 第一次打开的时候用户列表没有加载完
            setTimeout(() => {
              Object.keys(usersList.value).reduce((data, key) => {
                if (contains(logined, String(key))) {
                  usersList.value[key].login = true;
                } else {
                  usersList.value[key].login = false;
                }
              }, {});
            }, 200);
          }
        };

        //连接关闭的回调方法
        websocket.onclose = function () {
          console.log('关闭连接');
        };
      };
      //判断数组中是否存在某个值
      function contains(arr, val) {
        if (!arr) {
          return false;
        }
        for (var i = 0; i < arr.length; i++) {
          if (arr[i] == val) {
            return true;
          }
        }
        return false;
      }
      // 关闭socket
      const closeSocket = () => {
        websocket.close();
      };
      // 初始化表情列表
      const initEmoji = () => {
        const key = 'EMOJI_LIST';
        //初始化本地缓存对象
        const fn_get = Persistent.getLocal;
        //判断对象是不是已经加载
        const emojis = fn_get(key);
        const fn_set = Persistent.setLocal;
        if (emojis === undefined || JSON.stringify(emojis) == '{}') {
          //读取表情文件并放入缓存中
          const files = import.meta.globEager('./emojis/*.gif');
          const emojilib = {};
          for (let key in files) {
            const url = files[key].default;
            const id = '[emo#' + url.split('/').slice(-1)[0].split('.')[0] + ']';
            emojilib[id] = url;
          }
          //缓存信息
          emojisList.value = emojilib;
          fn_set(key, emojilib, true);
        } else {
          emojisList.value = emojis;
          // fn_set(key, {}, true);
        }
      };
      // 点击表情事件
      const clickEmo = (name) => {
        popoverVisible.value = false;
        messageText.value = messageText.value + name;
        //焦点还原到输入框中
        textareaRef.value.focus();
      };
      //是否显示叹号
      function showImg(loading, success) {
        if (loading === false && success === false) {
          return 'inline-block';
        } else {
          return 'None';
        }
      }
      //是否显示加载圈
      function showLoading(loading) {
        if (loading === false) {
          return 'None';
        } else {
          return 'inline-block';
        }
      }
      // 显示信息动态解析
      const showMessage = (value) => {
        const message = value.message;
        const isLoginBoard = value.isLoginBoard;
        const isSelf = value.isSelf;
        const loading = value.loading;
        const success = value.success;
        const senderId = value.senderId;

        const sds = message.split(/(\[emo#\d+\])/);
        let mesShow = '';
        const showType = showImg(loading, success);
        const showLoad = showLoading(loading);
        const divStyle =
          '<div style="background-color:#e9e7e7;display:inline-block;text-align:left;white-space: pre-wrap;white-space: -moz-pre-wrap;' +
          'white-space: -pre-wrap;white-space: -o-pre-wrap;word-wrap: break-word;width: fit-content;width: -webkit-fit-content;' +
          'width: -moz-fit-content;max-width: 500px;min-height: 31px;font-size: 16px;padding-left: 5px;padding-right: 5px;margin: 5px;' +
          'border: #222222 2px solid;border-radius: 5px;">';
        if (isLoginBoard) {
          mesShow =
            mesShow +
            '<div style="width:100%;text-align:center">' +
            '<p style="background-color:#e9e7e7;font-size:12px;border:1px black solid;border-radius: 10px;">' +
            '欢迎进入聊天室</p></div>';
        } else {
          if (isSelf) {
            mesShow +=
              '<div style="width:100%;text-align:right">' +
              '<img style="height:26px;width:26px;display:' +
              showType +
              '" src="' +
              errorImg +
              '"/>' +
              '<img style="height:22px;width:22px;display:' +
              showLoad +
              '" src="' +
              loadingImg +
              '"/>' +
              divStyle;
          } else {
            mesShow +=
              '<div style="width:100%:height:100px">' +
              '<div style="padding-left:42px;color:#7c7a7a;font-size:12px;font-weight:600">' +
              // '在这里是名字' +
              usersList.value[senderId]?.realname +
              '</div><img style="float:left;height:35px;width:40px;padding-left: 5px;"></img>' +
              divStyle;
          }

          var re = new RegExp(/(\[emo#\d+\])/);
          for (let i = 0; i < sds.length; i++) {
            //拼接内部显示的东西
            if (sds[i].length === 0) {
              continue;
            }
            if (sds[i].match(re) && emojisList.value.hasOwnProperty(sds[i])) {
              // 表情替换
              mesShow =
                mesShow +
                '<img style="height:26px;width:26px;display:inline-block" src="' +
                emojisList.value[sds[i]] +
                '"/>';
            } else {
              // 直接拼接
              mesShow = mesShow + sds[i];
            }
          }
          // 处理头像
          if (isSelf) {
            // 在右边
            mesShow =
              mesShow +
              '</div><img style="display:inline-block;float:right;height:35px;width:40px;padding-right: 5px;"></img></div>';
          } else {
            mesShow = mesShow + '</div></div>';
          }
        }

        setTimeout(() => {
          messageBoxRef.value.scrollTop = messageBoxRef.value.scrollHeight;
        }, 20);
        return mesShow;
      };
      // 点击发送事件
      const clickSend = () => {
        const message = messageText.value;
        if (message.trim().length === 0) {
          // notification.error({
          //   description: '不能发送空信息',
          //   duration: 3,
          // });
          textareaRef.value.focus();
          return;
        }
        //解析消息中的表情
        const uuid = guid();
        messageList.value[uuid] = {
          isSelf: true,
          message: message,
          loading: true,
          success: false,
        };
        //调用消息接口传消息
        try {
          const param = {};
          param.uuid = uuid;
          param.message = message;
          param.receiverId = currentTalkUserId.value;
          wsInit();
          websocket.send(JSON.stringify(param));

          //消息发送之后
          //十五秒后判断当前 uuid 的消息是否已经成功 若loading仍是true 那么就认为是失败了
          //后台不会放弃该任务 所以可能会导致 前端已经判断消息发送失败，前端显示感叹号 但是后端在15秒之后发送成功 
          //此时消息状态会再次被修改
          setTimeout(() => {
            if (messageList.value[uuid].loading === true) {
              messageList.value[uuid].loading = false;
              messageList.value[uuid].success = false;
            }
          }, 15000);
        } catch (e) {
          messageList.value[uuid].success = false;
        }
        //焦点还原到输入框中
        messageText.value = '';
        textareaRef.value.focus();
      };
      //guid生成
      function guid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
          var r = (Math.random() * 16) | 0,
            v = c == 'x' ? r : (r & 0x3) | 0x8;
          return v.toString(16);
        });
      }
      //点击用户或聊天室
      const clickCard = (index) => {
        if (currentTalkUserId.value !== index) {
          currentTalkUserId.value = index;
          usersList.value[index].unread = 0;
          changeUsers();
        }
      };
      //筛选用户
      const filterUserList = (type, sv) => {
        let filterData = {};
        Object.keys(usersList.value).reduce((data, key) => {
          if (usersList.value[key].type === type) {
            if (sv && usersList.value[key].realname.indexOf(sv) > -1) {
              filterData[key] = usersList.value[key];
            } else if (!sv) {
              filterData[key] = usersList.value[key];
            }
          }
        }, {});
        return filterData;
      };
      //切换用户的操作
      async function changeUsers() {
        //点击好友或者群组之后，清空当前聊天面板并 =查询历史记录（待更新）
        //先清空当前面板
        messageList.value = {};
        //查询对应用户的聊天信息并加载（TODO）
        const message = await getMessListApi(currentTalkUserId.value);
        if (message) {
          for (let i = 0; i < message.length; i++) {
            const uuid = guid();
            const data = message[i];
            function isSelfFunc() {
              return data.senderId == currentUser.value.userId;
            }
            messageList.value[uuid] = {
              isSelf: isSelfFunc(),
              message: data.message,
              senderId: data.senderId,
              receiverId: data.receiverId,
              sendTime: data.sendTime,
              loading: false,
              success: true,
            };
          }
        }
      }
      const onSearch = () => {
        // console.log('searchValue', searchValue);
      };
      return {
        initUserList,
        emojisList,
        initEmoji,
        messageText,
        clickEmo,
        popoverVisible,
        textareaRef,
        messageBoxRef,
        clickSend,
        showMessage,
        messageList,
        wsInit,
        usersList,
        closeSocket,
        currentTalkUserId,
        clickCard,
        filterUserList,
        searchValue,
        onSearch,
      };
    },
    mounted() {
      //初始化用户列表
      this.initUserList();
      //初始化表情
      this.initEmoji();
      //初始化ws
      this.wsInit();
    },
    beforeUnmount() {
      this.closeSocket();
    },
  });
</script>
<style lang="less" scoped>
  .chat_horline {
    width: 100%;
    height: 2px;
    background-color: rgb(49, 49, 49);
  }
  .content {
    display: flex;
    width: 100%;
    height: calc(100% - 4px);
    &__ {
      &left {
        width: 220px;
        height: 100%;
        flex-shrink: 0;
        &_chatroom {
          width: 220px;
          border-radius: 10px;
        }
        &_user_searcher {
          //预留一个搜索框的位置
          height: 40px;
          padding: 5px;
          background-color: #fffbfb;
        }
        &_user {
          width: 220px;
          position: absolute;
          top: 97px; //要算上边框的宽度
          bottom: 0px;
          overflow-y: auto;
          &::-webkit-scrollbar {
            display: none;
          }
        }
        &_item {
          padding: 5px;
          padding-top: 10px;
          color: #444444;
          font-weight: 600;
          margin-top: 2px;
          margin-left: 3px;
          margin-right: 3px;
          width: auto;
          height: 55px;
          background-color: transparent;
          border-radius: 10px;
          &:hover {
            background-color: rgb(180, 180, 180);
          }
        }
        &_item_selected {
          padding: 5px;
          padding-top: 10px;
          margin-left: 3px;
          margin-right: 3px;
          width: auto;
          color: #444444;
          font-weight: 600;
          margin-top: 2px;
          height: 55px;
          background-color: transparent;
          border-radius: 10px;
          background-color: rgb(200, 200, 200);
        }
      }
      &verline {
        width: 2px;
        flex-shrink: 1;
        background-color: rgb(49, 49, 49);
      }
      &right {
        flex: 2;
        background-color: whitesmoke;
        border-top-right-radius: 5px;
        border-bottom-right-radius: 5px;
        &_room {
          width: 100%;
          height: 100%;
          .chat_content_parent {
            position: relative;
            height: calc(100% - 162px);
            ::-webkit-scrollbar {
              display: none;
            }
          }
          .chat_content_child {
            position: absolute;
            width: 100%;
            overflow-y: auto;
            height: 100%;
            background-color: whitesmoke;
            padding: 5px;
          }
          .chat_line {
            width: 100%;
            height: 2px;
            background-color: rgb(49, 49, 49);
          }
          .chat_sender {
            padding: 5px;
            width: 100%;
            height: 160px;
            .sender_header {
              height: 30px;
              width: 100%;
              .btn_right {
                color: whitesmoke !important;
                font-weight: 600;
                float: right;
                margin-left: 8px;
                color: rgb(36, 36, 36);
                background: rgb(36, 36, 36);
                border: #222222 2px solid;
              }
              .icon_left {
                float: left;
                margin-right: 4px;
                font-size: 24px;
                color: rgb(36, 36, 36);
                background: whitesmoke;
              }
            }
            textarea {
              padding: 3px;
              color: rgb(24, 23, 23);
              resize: none;
              width: 100%;
              height: calc(100% - 30px);
              background-color: whitesmoke;
              border: #222222 2px solid;
              border-radius: 5px;
            }
          }
        }
      }
      &components {
        flex-shrink: 3;
        width: 50px;
        height: 100%;
        border-top-right-radius: 5px;
        border-bottom-right-radius: 5px;
        background-color: whitesmoke;
      }
    }
  }
  // 在代码中书写 因为会影响其他页面所以 样式使用scoped限制 但是scoped的会导致动态生成的元素样式失效
  .textMessage {
    // 原文本格式显示 识别空格 并且自动换行
    white-space: pre-wrap;
    white-space: -moz-pre-wrap;
    white-space: -pre-wrap;
    white-space: -o-pre-wrap;
    // 自动换行
    word-wrap: break-word;
    // 宽度自适应内容
    width: fit-content;
    width: -webkit-fit-content;
    width: -moz-fit-content;
    // 最大宽度
    max-width: 500px;
    min-height: 31px;
    font-size: 16px;
    padding-left: 5px;
    padding-right: 5px;
    margin: 2px;
    border: #e9e7e7 2px solid;
    border-radius: 5px;
  }
</style>


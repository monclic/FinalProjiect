<template>
  <div style="margin-top: 100px;">
    <h1>聊天室</h1>
    <div v-if="connected">
      <ul>
        <li v-for="message in messages" :key="message.id">
          <strong>{{ message.sender }}</strong>: {{ message.content }}
        </li>
      </ul>
      <div>
        <input v-model="newMessage" placeholder="输入消息" />
        <button @click="sendMessage">发送</button>
      </div>
    </div>
    <div v-else>
      <p>正在连接到聊天室...</p>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue';
import { Stomp, CompatClient,Message  } from '@stomp/stompjs';

export default defineComponent({
  data() {
    return {
      connected: false,
      messages: [] as Message[],
      newMessage: '',
      stompClient: null as CompatClient | null,
    };
  },
  created() {
    // 建立 WebSocket 连接
    this.stompClient = Stomp.client('ws://localhost:8080/ws/chat/');
    this.stompClient.debug = () => {};

    // 监听连接状态变化
    this.stompClient.onConnect = () => {
      this.connected = true;
      console.log('WebSocket 连接已建立');

      // 订阅消息目标
      this.stompClient?.subscribe('/topic/messages', (response) => {
        const message = JSON.parse(response.body);
        this.messages.push(message);
      });
    };

    // 连接到 WebSocket 服务器
    this.stompClient?.connect({}, () => {
      // 连接成功回调
      console.log('已连接到 WebSocket 服务器');
    });
  },
  methods: {
    sendMessage() {
      if (this.newMessage) {
        const message = {
          sender: 'User',
          content: this.newMessage,
        };
        this.stompClient?.send('/app/sendMessage', {}, JSON.stringify(message));
        this.newMessage = '';
      }
    },
  },
});
</script>

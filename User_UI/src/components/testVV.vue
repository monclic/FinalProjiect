<template>
  <div>
    <div class="e" v-show="showDiv"></div>
    <b @toggle-div="handleToggleDiv"></b>
  </div>
</template>

<script lang="ts" setup>
import b from './test.vue';
import { ref } from 'vue';

const showDiv = ref(false);

const handleToggleDiv = (payload:any) => {
  // 处理来自 b.vue 的自定义事件
  // 在此处根据需求修改 div.e 的显示或隐藏逻辑
  showDiv.value = !showDiv.value;

  // 在这里可以使用 payload 参数传递的值
};
</script>

<style scoped>
</style>

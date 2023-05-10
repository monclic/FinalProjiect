<template>
  <div class="c" @click="toggleDiv"></div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'b',
  emits: ['toggle-div'], // 声明自定义事件

  methods: {
    toggleDiv() {
      const payload = 'your parameter'; // 替换为要传递的参数
      this.$emit('toggle-div', payload); // 触发自定义事件并传递参数
    }
  }
});
</script>

<style scoped>
</style>

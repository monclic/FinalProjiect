<template>
  <div class="scrollable" ref="scrollable" @scroll="handleScroll">
    <div v-for="(item, index) in items" :key="index" class="item">
      {{ item }}
    </div>
    <div v-if="loading" class="loading">Loading...</div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue';

const scrollable = ref<HTMLDivElement | null>(null);
const items = reactive(Array.from({ length: 20 }, (_, i) => `Item ${i + 1}`));
const loading = ref(false);
const handleScroll = () => {
    if (
      scrollable.value &&
      scrollable.value.scrollTop + scrollable.value.clientHeight >= scrollable.value.scrollHeight - 10
    ) {
      if (!loading.value) {
        loading.value = true;
        setTimeout(() => {
          items.push(`Item ${items.length + 1}`);
          loading.value = false;
        }, 500); //加载动画5s
      }
    }
  };

onMounted(() => {
  scrollable.value?.addEventListener('scroll', handleScroll);

  return () => {
    scrollable.value?.removeEventListener('scroll', handleScroll);
  };
});
</script>

<style scoped>
.scrollable {
  height: 300px;
  overflow-y: auto;
  padding-bottom: 10px;
}

.item {
  height: 100px;
  border: 1px solid gray;
  margin: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.loading {
  text-align: center;
  margin: 10px;
  font-size: 20px;
  color: gray;
}
/* 隐藏滚动条 */
::-webkit-scrollbar {
  width: 0;
  height: 0;
}

/* 隐藏滚动条上的滑块 */
::-webkit-scrollbar-thumb {
  display: none;
}

/* 隐藏滚动条的背景 */
::-webkit-scrollbar-track {
  display: none;
}

</style>

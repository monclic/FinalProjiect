<template>
  <div>
    <div v-for="(item, index) in items" :key="index" class="item">
      <div style="height: 100px;"></div>{{ item }}</div>
    <div ref="sentinel" class="sentinel"></div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';

const items = ref([1, 2, 3]);
const sentinel = ref<HTMLDivElement | null>(null);
const loading = ref(false);

const observer = new IntersectionObserver(([entry]) => {
  if (entry.isIntersecting) {
    loadMore();
  }
});

onMounted(() => {
  console.log(sentinel.value);
  
  if (sentinel.value) {
    observer.observe(sentinel.value);
  }
});

onUnmounted(() => {
  observer.disconnect();
});

const loadMore = async () => {
  if (loading.value) return;
  loading.value = true;
  await nextTick();
  items.value.push(...[1, 2, 3]);
  loading.value = false;
};

</script>

<style scoped>
.scrollable {
  /* height: 100vh; */
  overflow-y: auto;
}
.item {
  /* height: 150px; */
  background-color: #ddd;
  margin-bottom: 10px;
}
.sentinel {
  height: 20px;
}
</style>

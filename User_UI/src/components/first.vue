<template>
  <div :style="backgroundImageStyle"></div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'

const imageUrl = ref('https://picsum.photos/200/300')
const backgroundImageStyle = computed(() => ({
  backgroundImage: `url(${imageUrl.value}?image_process=format,webp/quality,q_80/resize,w_350,h_238/crop,mid,w_350,h_238)`
}))
</script>

<template>
  <button @click="cc">1111111</button>
<short_publish ref="mref"></short_publish>
</template>

<script lang='ts' setup>
import { defineComponent, ref } from 'vue';
import MyComponent from './testVV.vue';
import short_publish from './Short/short-publish.vue'
const mref =  ref<any>();
function cc(){
  mref.value?.dialogShow()
}

</script>

<style scoped>

</style>
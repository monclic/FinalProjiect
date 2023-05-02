<template>
  <p>2222222222</p>
  <span>1111110{{ message }}</span>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue';

export default defineComponent({
  name: 'MyComponent',

});
</script>
<script lang='ts' setup>
 const message = ref(1);
   function c(){
      message.value+=1;
    }
    // defineExpose({ c })
    // 把 message 暴露出去
    defineExpose({ c ,message})
</script>
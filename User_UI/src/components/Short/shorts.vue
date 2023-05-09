<template>
  <div id="shorts-core">
    <router-view />
  </div>
</template>
  
<script lang='ts'>
import { defineComponent } from 'vue';
import http from '../../utils/http';
import internal from 'stream';

export default defineComponent({
  name: 'testVV',
});

</script>
<script lang='ts' setup>

</script>

<style scoped>
#shorts-core {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  margin-right: 0;
}






.shorts-context-wrapper {
  width: 100%;
}
</style>
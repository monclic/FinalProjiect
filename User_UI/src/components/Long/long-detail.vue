<template>
<div class="long-detail">
<div class="long-detail-main">
    <v-md-editor v-model="text" mode="preview" ></v-md-editor>

     <!-- 回复区域 -->
     <div class="reply-space">
                <replys :to-context-id="id" :to-context-type="1"></replys>
            </div>
</div>
<div class="long-detail-aside">

</div>
</div>
</template>

<script lang='ts' setup>
import replys from '../reply/replys.vue'
import { useRoute } from 'vue-router';
import http from '../../utils/http';
import { reactive, ref } from 'vue';
import { onMounted } from 'vue';

const router = useRoute()
const state = reactive({
    items: [] as any
});
const id = ref(router.params.lid)
const text=ref('')
const getLongDetail= async () => {
    let param = { longId: router.params.lid }
    http.get("/Longs/Long", param)
        .then((data: any) => {
            state.items = data
            text.value=data.text                        
        })
}
onMounted(()=>{
    getLongDetail()
})
</script>

<style scoped>
.long-detail{
    width: 100%;
    display: flex;
    flex-direction: column;
}
</style>
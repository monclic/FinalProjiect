<template>
    <div class="replys-container">
        <div class="replys-container-wrapper">
            <div class="reply-publish-wrapper">
                <reply_publish_first :to-context-id="toContextId" :to-context-type="type"></reply_publish_first>
            </div>
            <div v-for="(item, index) in state.items" :key="index" class="reply-list">
            <reply_item @toggle-div="handleToggleDiv" :item="item" :index="index"></reply_item>
            <div v-show="item.show_reply_publish" class="reply-item-publish">
          <reply_publish :to-context-id="item.id" :to-context-type="0"></reply_publish>
        </div>
            <div class="bottom-line"></div>
        </div>
        </div>
    </div>
</template>


<script lang='ts'>
import { defineComponent, onMounted, reactive ,provide} from 'vue';
import http from '../../utils/http';

export default defineComponent({
    name: 'replys',
});

</script>
<script lang='ts' setup>
import { ref, defineProps } from 'vue'
import reply_publish_first from './reply_publish_first.vue'
import reply_publish from './reply_publish.vue'
import reply_item from './reply-item.vue'

// 定义组件的 props
const props = defineProps({
    toContextId: {
        type: String,
        required: false
    },
    toContextType:{
        type:Number,
        required:false,
    },
    children: {
        type: Array,
        required: false
    }
});
const id=ref(parseInt(props.toContextId))
const type=ref(props.toContextType)
const isLike = ref(false)
const setLIke = () => {
    isLike.value = !isLike.value
}

const state = reactive({
    items: [] as any
});

const getReplys = async () => {
    if (!props.children || props.children.length == 0) {
        let param = { contextId: props.toContextId, contextType:props.toContextType }            
        http.get("/replys/ReplyList", param)
            .then((data: any) => {
                state.items = data.list.map((item:any)=>{
                    return {...item,show_reply_publish:false}
                })                                                
            })
    } else {
        state.items = props.children
    }
}
const index=ref(-1)
const to_name=ref('')
const is_to_user=ref(false)
const handleToggleDiv = (payload:any) => {
  // 处理来自 b.vue 的自定义事件  
  // 在这里可以使用 payload 参数传递的值
if(index.value!=-1){
    state.items[index.value].show_reply_publish=false
}
state.items[payload.index].show_reply_publish=true

index.value=payload.index
to_name.value=payload.to_name
is_to_user.value=payload.is_to_user
};
const getType=()=>{
    if(is_to_user.value){
        return props.toContextType
    }else{
        return 0
    }
}
provide('aaa',to_name)

onMounted(() => {
    getReplys()
});
</script>

<style scoped>
.replys-container {
    width: 100%;
    padding: 20px;

}

.replys-container-wrapper {
    border: 12px solid rgba(255, 255, 255, 0.5);
    border-radius: 6px;
    box-sizing: border-box;
}
.reply-publish-wrapper{
    width: 100%;
    padding: 10px 0 20px;
    background-color: rgba(255, 255, 255, 0.5);
}
 .bottom-line {
    margin-left: 80px;
    border-bottom: 1px solid #E3E5E7;
    background-color: #fff;
}
.reply-list{
    background-color: rgba(255, 255, 255, 0.5);
}
.reply-item-publish{
    margin: 10px 0;
}
</style>

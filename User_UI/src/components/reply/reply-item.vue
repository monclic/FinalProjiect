<template>
    <div class="replys">
        <div class="reply-wrapper">
            <div class="reply-avatar">
                <div :style="{ backgroundImage: 'url(' + state.item.avatar + ')' }" class="reply-avatar-imgage"></div>
            </div>
            <div class="reply-body">
                <div class="reply-header">
                    <div class="reply-title">
                        <span>{{ state.item.username }}</span>
                    </div>
                </div>
                <div class="reply-textarea">
                    {{ state.item.replyContext }}
                </div>
                <div class="reply-info">
                    <div class="time-location">{{ state.item.createtime }}</div>
                    <!-- 点赞 -->
                    <div @click="setLIke" :class="{ 'like': true, 'liked': isLike }">
                        <i></i>
                        {{ state.item.likes }}
                    </div>
                    <div class="write-reply" @click="toggleDiv(state.item.username,false)">回复</div>
                </div>

                <!-- 子回复区域 -->
                <div v-if="state.item.childReplyIs" class="child-reply">
                    <div v-for="(i, index) in state.item.childList">
                        <div class="reply-wrapper">
                            <div class="reply-avatar">
                                <div :style="{ backgroundImage: 'url(' + i.avatar + ')' }" class="reply-avatar-imgage">
                                </div>
                            </div>
                            <div class="reply-body">
                                <div class="reply-header">
                                    <div class="reply-title">
                                        <span>{{ i.username }}</span>
                                    </div>
                                </div>
                                <div class="reply-textarea">
                                    {{ i.replyContext }}
                                </div>
                                <div class="reply-info">
                                    <div class="time-location">{{ i.createtime }}</div>
                                    <!-- 点赞 -->
                                    <div @click="setLIke" :class="{ 'like': true, 'liked': isLike }">
                                        <i></i>
                                        {{ i.likes }}
                                    </div>
                                    <div class="write-reply" @click="toggleDiv(i.username,true)">回复</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>


<script lang='ts'>
import { defineComponent, onMounted, reactive } from 'vue';
import replys_item from './reply-item.vue'

export default defineComponent({
    name: 'replys_item',
    emits: ['toggle-div'], // 声明自定义事件

    methods: {
        toggleDiv(name:any,is_to_user:boolean) {
            name='回复 '+name+':'
            const payload = {index:this.index,to_name:name,is_to_user:is_to_user}; // 替换为要传递的参数
            this.$emit('toggle-div', payload); // 触发自定义事件并传递参数
        }
    }
});

</script>
<script lang='ts' setup>
import { ref, defineProps } from 'vue'

// 定义组件的 props
const props = defineProps({
    item: {
        type: Array,
        required: true
    },
    index:{
        type:Number,
        required:true
    }
});
const isLike = ref(false)
const index=ref(-1)
const setLIke = () => {
    isLike.value = !isLike.value
}

const state = reactive({
    item: [] as any
});


onMounted(() => {
    state.item = props.item
    index.value=props.index
});
</script>

<style scoped>
.replys {
    position: relative;
    width: 100%;
}

.reply-wrapper {
    min-width: 564px;
    position: relative;
    display: flex;
    flex-direction: row;
}

.reply-avatar {
    align-items: center;
    display: flex;
    height: 86.4px;
    justify-content: center;
    left: 0;
    position: absolute;
    top: 0;
    width: 86.4px;

}

.reply-avatar-imgage {
    width: 65px;
    height: 65px;
    cursor: pointer;
    background-color: blanchedalmond;
    background-size: cover;
}

.reply-body {
    display: flex;
    flex-grow: 1;
    flex-direction: column;
    background-color: #fff;
    padding: 0 10px 10px 88px;
}

.reply-header {
    margin-top: 12px;
}


.short-context-header {
    margin-top: 12px;
}

.reply-title {
    align-items: center;
    display: flex;
    height: 22px;
    margin: 1px 0 2px;
    width: max-content;
    padding-left: 20px;
}

.reply-title span {
    color: #f69;
    font-size: 20px;
    line-height: 32px;
    font-weight: 500;
}

.reply-textarea {
    /* 当一个单词过长无法完整放入一行时，将该单词拆分为多行显示。 */
    word-wrap: break-word;
    box-sizing: border-box;
    font-size: 14px;
    letter-spacing: 1px;
    outline: none;
    padding: 10px 5px 0 6px;
    position: relative;
    vertical-align: baseline;
    /* 保留空格和换行符，自动换行。 */
    white-space: pre-wrap;
    width: 100%;
    /* 任意位置断开单词。 */
    word-break: break-all;
    padding-left: 20px;
}

.reply-info {
    color: #99a2aa;
    line-height: 14px;
    margin-top: 6px;
    font-size: 12px;
    display: flex;
    flex-direction: row;
}

.time-location {
    margin-right: 20px;
}

.like {
    cursor: pointer;
    margin-right: 20px;
}

.like i {
    display: inline-block;
    width: 14px;
    height: 14px;
    vertical-align: text-top;
    margin-right: 5px;
    background: url(../../public/icons.png) no-repeat;
    background-position: -153px -25px;
}

.like:hover i {
    background-position: -218px -25px;
}

.like.liked i {
    background-position: -154px -89px;
}

.write-reply {
    padding: 0 5px;
    border-radius: 4px;
    margin-right: 15px;
    cursor: pointer;
    display: inline-block;
    transform: color 0.3s;
}

.write-reply:hover {
    color: #94d6ef;
}
</style>
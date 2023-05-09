<template>
    <div class="replys">
        <div v-for="(item,index) in items" :key="index" class="reply-list">
            <div class="reply-wrapper">
        <div class="reply-avatar">
            <div class="reply-avatar-imgage"></div>
        </div>
        <div class="reply-body">
            <div class="reply-header">
                <div class="reply-title">
                    <span>{{ username }}</span>
                </div>
            </div>
            <div class="reply-textarea">
                {{ replyContext }}
            </div>
            <div class="reply-info">
                <div class="time-location">{{ createtime }}</div>
                <!-- 点赞 -->
                <div @click="setLIke" :class="{ 'like': true, 'liked': isLike }">
                    <i></i>
                    {{ likes }}
                </div>
                <div class="write-reply">回复</div>
            </div>

            <div v-if="childReplyIs" class="child-reply">
                <!-- 传个子表 -->
                <replys></replys>
            </div>
        </div>
    </div>
                       
        </div>
    </div>
    <div @click="getReplys" style="height: 50px;width: 200px;background-color: aquamarine;"></div>
</template>

<script lang='ts'>
import { defineComponent, onMounted } from 'vue';
import http from '../../utils/http';

export default defineComponent({
    name: 'replys',
});

</script>
<script lang='ts' setup>
import { ref, defineProps } from 'vue'

const isLike = ref(false)
const setLIke = () => {
    isLike.value = !isLike.value
}

const props = defineProps({
    parent_reply_id: {
        type: Number,
        required: true
    }
})
const getReplys = async () => {
    // const id = props.parent_reply_id;
    // console.log(id);
    let param={contextId:'6',contextType:'2'}
    http.get("/replys/ReplyList",param)
    .then((data:any)=>{
        console.log(data);
        
    })
}
const is_child_replys = ref(false)

onMounted(() => {
    getReplys();
});
</script>

<style scoped>
.replys {
    position: relative;
    width: 100%;
    padding: 20px;
}

.reply-wrapper {
    border: 12px solid rgba(255, 255, 255, 0.5);
    border-radius: 6px;
    box-sizing: border-box;
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
}

.reply-body {
    display: flex;
    flex-grow: 1;
    flex-direction: column;
    background-color: #fff;
    padding: 0 10px 10px 88px;
}

.reply-header {
    height: 62px;
    margin-top: 12px;
}


.short-context-header {
    height: 62px;
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
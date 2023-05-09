<template>
    <div class="short-detailed">
        <div class="short-detailed-main">
            <div class="short-context">
                <div class="shrot-wrapper">
                    <div class="short-avatar">
                        <div class="bili-dyn-avatar">
                            <!-- 头像 -->
                        </div>
                    </div>
                    <div class="short-context-body">
                        <div class="short-context-header">
                            <div class="bili-dyn-title">
                                <span>{{ username }}</span>
                            </div>
                        </div>
                        <div class="short-context-textarea"></div>
                        <!-- 图片区域 -->
                        <div class="short-images">
                            <div v-for="(item, index) in items" :key="index" class="short-images-wrapper">
                                <div class="short-images-item" :style="{ backgroundImage: 'url(/' + item.img + ')' }"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="reply-space">
                <reply :parent_reply_id="1" />
            </div>
        </div>
        <div class="short-detailed-aside">
            <div class="aside-wrapper">
                <!-- 点赞 -->
                <div @click="setLIke" :class="{ 'like': true, 'liked': isLike }">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd" clip-rule="evenodd"
                            d="M13.9252 3.04546C13.6432 3.01258 13.382 3.13521 13.2422 3.29868C12.9774 3.60848 12.8705 3.86276 12.7384 4.23799C12.7234 4.28037 12.7082 4.32422 12.6925 4.36961C12.5611 4.74857 12.3929 5.23391 12.044 5.85187C11.446 6.91117 10.8882 7.55173 10.2013 8.154C9.63865 8.6473 9.06639 8.98294 8.5 9.14275V19.7248C9.28689 19.7417 10.1287 19.7525 11 19.7525C13.2468 19.7525 15.1529 19.6812 16.4372 19.615C17.3477 19.5681 18.1379 19.1176 18.5497 18.3851C19.1274 17.3574 19.8396 15.8503 20.2712 14.0753C20.6734 12.4212 20.8569 11.0615 20.9392 10.1025C20.9774 9.65797 20.6256 9.25003 20.08 9.25003H15.0977C14.8476 9.25003 14.614 9.12542 14.4748 8.91775C14.3355 8.71015 14.3089 8.44684 14.4037 8.21557C14.4037 8.21555 14.4037 8.21553 14.4037 8.21551C14.4037 8.21547 14.4038 8.21543 14.4038 8.21539C14.4038 8.21537 14.4038 8.21536 14.4038 8.21534L14.4046 8.21341L14.4085 8.20377L14.4249 8.16256C14.4395 8.12573 14.4609 8.07081 14.4874 8.00089C14.5404 7.86084 14.6131 7.66185 14.6909 7.42856C14.8489 6.95439 15.0177 6.36917 15.0941 5.85681C15.2109 5.07451 15.1824 4.44592 14.8757 3.86439C14.5461 3.23954 14.1724 3.07429 13.9252 3.04546ZM16.164 7.75003H20.08C21.4037 7.75003 22.5555 8.81291 22.4337 10.2309C22.3455 11.2583 22.1508 12.6941 21.7288 14.4297C21.2555 16.3758 20.4798 18.0127 19.8573 19.1201C19.1594 20.3616 17.8654 21.0435 16.5144 21.113C15.21 21.1802 13.2777 21.2525 11 21.2525C8.7933 21.2525 6.77664 21.1846 5.34776 21.1195C3.73985 21.0461 2.39101 19.8517 2.21798 18.2152C2.1042 17.1391 2 15.7467 2 14.2525C2 12.8835 2.08746 11.6418 2.18985 10.6567C2.36874 8.93544 3.84615 7.75003 5.50754 7.75003H7.75C8.06896 7.75003 8.56382 7.59478 9.21241 7.02612C9.77509 6.53279 10.2246 6.0235 10.7378 5.11442C11.0177 4.61868 11.146 4.24973 11.2753 3.87788C11.2913 3.83198 11.3072 3.78604 11.3235 3.73981C11.4791 3.29802 11.6602 2.84083 12.1021 2.32398C12.5582 1.79047 13.3077 1.4633 14.0989 1.55555C14.9248 1.65186 15.6866 2.18668 16.2024 3.16456C16.7233 4.15199 16.7157 5.15349 16.5777 6.07814C16.4916 6.65522 16.3202 7.26593 16.164 7.75003ZM7 19.6835V9.25003H5.50754C4.54442 9.25003 3.77423 9.92257 3.68182 10.8118C3.58359 11.7569 3.5 12.9457 3.5 14.2525C3.5 15.6802 3.59975 17.0179 3.70967 18.0575C3.80099 18.9212 4.51252 19.5798 5.41611 19.621C5.88508 19.6424 6.41785 19.6641 7 19.6835Z"
                            fill="currentColor"></path>
                    </svg>
                    <div class="side-toolbar__action__text">{{ likes }}</div>
                </div>
                <div @click="get_short_detail" class="to-reply">
                    <!-- 回复区域 -->
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path fill-rule="evenodd" clip-rule="evenodd"
                            d="M2 11C2 6.58172 5.58172 3 10 3H14C18.4183 3 22 6.58172 22 11C22 15.2656 18.6616 18.7514 14.4549 18.9873C13.4971 20.2148 11.9228 21.7529 9.71136 22.3555C9.2837 22.472 8.89202 22.2894 8.673 22.0075C8.46064 21.7342 8.38542 21.3424 8.5439 20.9775C8.87531 20.2145 8.94851 19.5143 8.90558 18.9257C5.00479 18.3918 2 15.0471 2 11ZM10 4.5C6.41015 4.5 3.5 7.41015 3.5 11C3.5 14.459 6.20234 17.2875 9.6105 17.4885C9.95008 17.5086 10.2337 17.7547 10.3013 18.088C10.4401 18.7716 10.4842 19.6217 10.2587 20.5643C11.6947 19.9026 12.7693 18.7583 13.4679 17.806C13.6076 17.6155 13.829 17.502 14.0652 17.4997C17.6249 17.4648 20.5 14.5681 20.5 11C20.5 7.41015 17.5899 4.5 14 4.5H10Z"
                            fill="currentColor"></path>
                    </svg>
                    <div class="side-toolbar__action__text">{{ replys }}</div>
                </div>
            </div>
        </div>
    </div>
</template>
<script lang='ts'>
import { defineComponent } from 'vue';
import http from '../../utils/http';
import { da } from 'element-plus/es/locale';

export default defineComponent({
    name: 'short_detail',
});

</script>
<script lang='ts' setup>
import reply from '../reply/replys.vue'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router';

const isLike = ref(false)
const setLIke = () => {
    isLike.value = !isLike.value
}
const router = useRouter()

interface short_detail{
    id:number,
    imagesList:[],
    imagesIs:boolean,
    shortContent:String,
    createTime:String,
    likes:number,
    replys:number
}
// const items=ref<short_detail>()
const items={}
const get_short_detail = async() => {
 // let param={shortId:router.query?.id}
let param = { shortId: '16' }
http.get("/shorts/Short", param)
      .then((data: any) => {
        items.value=data.goods             
        console.log(items.value?.id);
        
     })
}
onMounted(() => {
    get_short_detail
})
</script>

<style scoped>
.short-detailed {
    width: 100%;
    display: flex;
    flex-direction: row;
}

.short-detailed-main {
    display: flex;
    flex-direction: column;
    flex-grow: 1;
    /* width: 100%; */
    margin-right: 0;
}

.short-context {
    position: relative;
    overflow: hidden;
    width: 100%;
    padding: 20px;
}

.shrot-wrapper {
    border: 12px solid rgba(255, 255, 255, 0.5);
    border-radius: 6px;
    box-sizing: border-box;
    font-weight: 400;
    letter-spacing: 0;
    min-width: 564px;
    position: relative;
    display: flex;
    flex-direction: row;
}

.short-context-body {
    display: flex;
    flex-grow: 1;
    flex-direction: column;
    background-color: #fff;
    padding: 0 10px 0 88px;
}

.short-avatar {
    align-items: center;
    display: flex;
    height: 86.4px;
    justify-content: center;
    left: 0;
    position: absolute;
    top: 0;
    width: 86.4px;

}

.bili-dyn-avatar {
    width: 65px;
    height: 65px;
    cursor: pointer;
    background-color: blanchedalmond;
}

.short-context-header {
    margin-top: 20px;
}

.bili-dyn-title {
    align-items: center;
    display: flex;
    height: 22px;
    margin: 1px 0 2px;
    width: max-content;
    padding-left: 20px;
}

.bili-dyn-title span {
    color: #f69;
    font-size: 20px;
    line-height: 32px;
    font-weight: 900;
}

.short-context-textarea {
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

.short-images {
    display: flex;
    flex-wrap: wrap;
    margin: 0;
    padding: 10px 0 0;
}

.short-images-wrapper {
    display: flex;
    flex: 0 0 auto;
    width: 25%;
    max-width: 100%;
    padding-right: calc(1rem* .5);
    padding-left: calc(1rem * .5);
    margin-bottom: 1.236rem;
    border-radius: 4px;
    overflow: hidden;
}

.short-images-item {
    height: 12rem;
    width: 100%;
    border-color: transparent;
    background-size: cover;
    background-position: center;
    opacity: 1;
    /* transform: translate(0); 的作用是将元素移动到水平和垂直方向上的位置都不发生变化。这个属性可以用来解决一些布局问题，比如父元素设置了display: flex;后子元素在某些情况下会发生一些意料之外的偏移。
在这种情况下，将子元素的transform属性设置为translate(0)，可以强制浏览器重新计算子元素的位置，从而避免偏移问题。 */
    transform: translate(0);
    transition: transform .25s, opacity .25s;
    border-radius: 6px;
}

.short-detailed-aside {
    order: 4;
    flex-shrink: 0;
    margin-top: 120px;
    margin-left: 0.236rem;
    overflow: hidden;
}

.aside-wrapper {
    border-radius: 6px;
    background-color: #fff;
    padding: 12px 6px;
}

.like {
    position: relative;
    cursor: pointer;
    width: 46px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    color: #9499a0;
}

.like:hover {
    color: #00a1d6 !important;
}

.like.liked svg path {
    fill-rule: nonzero;
}

.side-toolbar__action__text {
    user-select: none;
    margin-top: 4px;
    font-size: 12px;
    line-height: 14px;
    align-items: center;
}

.to-reply {
    margin-top: 1rem;
    position: relative;
    cursor: pointer;
    width: 46px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    color: #9499a0;
}

.to-reply:hover {
    color: #00a1d6 !important;
}</style>
<template>
    <nav class="nav-list">
        <!-- 使 Vue 根据 key 来判断元素的身份,这样在数据更新时就可以只更新相应的元素，提高渲染性能。 -->
        <!-- 滚动目录 -->
        <!-- <a v-for="(item,index) in items" :key="index" @click="scrollToSection(index)" :class="['nav-item', {'nav-item-bcolor': isActive(index),'nav-item-hover': !isActive(index)}]">
           <span> {{ item }}</span>
        </a>         -->

        <!-- 跳转目录  -->
        <div v-for="(item,index) in items" :key="index" :class="['nav-item', {'nav-item-bcolor': isActive(index),'nav-item-hover': !isActive(index)}]">
          <router-link :to='item'>item</router-link>
        </div>         
    </nav>
</template>

<script lang='ts'>
import { defineComponent ,ref,onMounted, onUnmounted } from 'vue';

export default defineComponent({
    name: 'Nav',
});

</script>

<script lang="ts" setup>

const items=['shorts','贰贰贰','叁叁叁','肆肆肆','伍伍伍']

// 定义响应式数据activeIndex
const activeIndex = ref(0)

// 判断当前选项卡是否被选中
const isActive = (index: number) => {
return activeIndex.value === index
}

// 设置当前选项卡为选中状态
const setActive = (index: number) => {
activeIndex.value = index 
}

// 1.点击目录进行内容滚动
const scrollToSection = (index: number) => { 
const pageScroll = document.querySelector("#content") as HTMLElement;   
if(pageScroll){
window.scrollTo({top:(<HTMLElement>pageScroll.children[index]).offsetTop,behavior:"smooth"});   
};
} 
// 2.监听页面滚动事件
const handleScroll = () => {
//  获取页面上所有的 section 元素
const sections = document.querySelectorAll('.section-wrapper')  

// 倒序循环每个 section 元素，依次判断其是否在视口中。
for (let i = sections.length - 1; i >= 0; i--) {
const section = sections[i]
// 对于每个 section 元素，使用 getBoundingClientRect() 方法获取其在视口中的位置信息，包括距离视口顶部的偏移量等。
const rect = section.getBoundingClientRect()

// 判断页面滚动距离是否超过当前选项卡的位置
// 400 是一个偏移量的值，用于使选中的选项卡能够在滚动到目标位置之前就已经被激活。
// 由于是倒序遍历，且符合条件直接退出，不会出现被第一个nav一直占用的情况
if (rect.top <=400) {     
  setActive(i)
  break
}
}
}

// 监听组件挂载事件
onMounted(() => {
window.addEventListener('scroll', handleScroll)

})

// 监听组件卸载事件
onUnmounted(() => {
window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.nav-list {
    width: 140px;
    padding: 0;
    margin: 0;
    position: fixed;
}

.nav-item {
    /* 解决子元素超出父元素的范围的问题 */
    /* box-sizing: border-box; */
    display: flex;
    align-items: center;
    position: relative;
    /* background-color: #fff; */
    width: 100%;
    /* em 单位是相对于当前元素的字体大小来计算的长度单位。如果一个元素没有定义字体大小，它将继承父元素的字体大小。 */
    height: 3em;
    margin-bottom: 0.618rem;
    padding: 0 1.236rem;
    border-radius: 3px;
    overflow: hidden;
    /* 用于去除文本的下划线或其他修饰效果 */
    text-decoration: none;
    /* 用于控制字母之间的距离 */
    letter-spacing: .5px;
    /* will-change用于告知浏览器某个元素将要发生改变，以便浏览器在元素状态改变之前进行优化，提升性能。 */
    will-change: background-color;
    transition: background-color .06s;
}
.nav-item-bcolor{
    background-color: #fff;
}
.nav-item span {
    color: #0088f5;
    font-size: 1.1rem;
    /* 标准的可选值为 100、200、300、400、500、600、700、800、900，数值越大代表字体越粗。超出无效 */
    font-weight: 900;
}

.nav-item-hover:hover {
    /* 悬停变色 */
    background-color: #f3f3f3;
}
</style>
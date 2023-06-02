<template>
    <nav class="nav-list">
        <!-- <div v-for="(item,index) in items" :key="index" :class="['nav-item', {'nav-item-actived': isActive(index)}]">
          <router-link style="text-decoration: none;" to='#'>{{item}}</router-link> @click="toggleDiv"
        </div>          -->
        <div v-for="(item,index) in items">
            <router-link @click="toggleDiv(item)" style="text-decoration: none;" :to="item.url">
                <div @click="setActive(index)" :class="['nav-item',{'nav-item-actived': isActive(index)}]">
                    {{ item.id }}
                </div>
            </router-link>
        </div>
    </nav>
</template>

<script lang='ts'>
import { defineComponent ,ref,onMounted, onUnmounted, provide } from 'vue';

export default defineComponent({
    name: 'U_Nav',
    emits: ['toggle-div'], // 声明自定义事件
    methods: {
    toggleDiv(item:any) {
      const payload = item.id; // 替换为要传递的参数
      this.$emit('toggle-div', payload); // 触发自定义事件并传递参数
    }
}
});
</script>

<script lang="ts" setup>

const items=[{url:'/long',id:'longs'},
{url:'/short',id:'shorts'},
{url:'/admins',id:'admins'},
{url:'/board',id:'board'}]
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
provide('title','111')
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
    width: 100%;
    /* em 单位是相对于当前元素的字体大小来计算的长度单位。如果一个元素没有定义字体大小，它将继承父元素的字体大小。 */
    height: 3em;
    margin-bottom: 0.618rem;
    padding-left: 40px;
    border-radius: 3px;
    overflow: hidden;
    /* 用于控制字母之间的距离 */
    letter-spacing: .5px;
    /* will-change用于告知浏览器某个元素将要发生改变，以便浏览器在元素状态改变之前进行优化，提升性能。 */
    will-change: background-color;
    transition: background-color .06s;
    color: #595e71;    
    font-weight: 700;
}
.nav-item:hover{
    color: #2faee3;
}
.nav-item-actived{
    color: #2faee3;
}
</style>
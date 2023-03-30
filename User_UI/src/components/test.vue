<template>
<!-- 参考：https://blog.csdn.net/m0_68324632/article/details/125988195 -->
  <div class="container">
    <nav>
      <ul>
        <!-- 如果 isActive(index) 返回 true，则该段落的 class 属性将被设置为 "active"，否则它将被设置为空字符串。 -->
        <li v-for="(item, index) in items" :key="index" @click="scrollToSection(index)"  :class="{ active: isActive(index) }">
         {{ item.title }}
        </li>
      </ul>
    </nav>
    <main class="main">
      <section v-for="(item, index) in items" :key="index" :id="item.id">
        <h2>{{ item.title }}</h2>
        <p>{{ item.content }}</p>
      </section>
    </main>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue'

// 定义数据类型
interface Item {
  title: string
  content: string
  id: string
}

// 定义一个包含标题、内容和ID的项目数组
const items: Item[] = [
  { title: 'Section 1', content: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.', id: 'section-1' },
  { title: 'Section 2', content: 'Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.', id: 'section-2' },
  { title: 'Section 3', content: 'Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.', id: 'section-3' },
  { title: 'Section 4', content: 'Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', id: 'section-4' },
]

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
  const pageScroll = document.querySelector(".main") as HTMLElement;   
  if(pageScroll){
    window.scrollTo({top:(<HTMLElement>pageScroll.children[index]).offsetTop,behavior:"smooth"});  
};

// 2.监听页面滚动事件
const handleScroll = () => {
  //  获取页面上所有的 section 元素
  const sections = document.querySelectorAll('section')  

// 获取页面滚动距离。window.pageYOffset、document.documentElement.scrollTop 和 document.body.scrollTop 分别对应了不同浏览器下获取页面滚动距离的方法，|| 0 避免了在不支持某种方法的浏览器下出现错误。
const scrollTop = window.scrollY

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
<style>
nav {
  position: fixed;
  top: 0;
  left: 0;
  width: 200px;
  height: 100vh;
  background-color: #f1f1f1;
  overflow: auto;
}

nav ul {
  list-style: none;
  margin: 0;
  padding: 0;
}

nav li {
  padding: 10px;
  transition: background-color 0.3s ease;
  cursor: pointer;
}

nav li.active {
  background-color: #ddd;
}

main {
  margin-left: 200px;
  padding: 20px;
}

section {
  margin-bottom: 200px;
  height: 1000px;
}
</style>

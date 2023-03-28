<template>
  <div id="container">
    <header id="header">
      <div><a href="#">Link 1</a></div>
      <div><a href="#">Link 1</a></div>
    </header>

    <main id="main">
      <!-- nav -->
      <div id="nav">
        <nav class="nav-list">
          <a class="nav-item">

            <span>111111</span>

          </a>
        </nav>
      </div>
      <!-- content -->
      <div id="content">
        <!-- for  -->
        <!-- div.item  -->
        <!-- template  -->
        <!-- /for -->
        <Section/>
        <Section/>
        </div>

     
    </main>

    <footer id="footer"></footer>
  </div>
</template>

<script lang='ts' setup>
import Section from './section.vue'
</script>

<style scoped>
#container {
  display: flex;
  flex-direction: column;
  width: 100%;
  /* 水平居中 */
  justify-content: center;
  /* 竖直居中 */
  align-items: center;
  padding-top: 4.6rem;
  background-color: #e7e7e7;
}

#header {
  height: 3.6rem;
  /* 固定 */
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  padding: 10px 300px 10px;
  background-color: #f5f5f5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* 置于最前 */
  z-index: 999;
  /* 透明度 */
  opacity: 0.5;
  border-bottom: 1px solid #f5f5f5;
}

#main {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  padding: 0 100px;
}

#nav {
  order: 1;
  flex-shrink: 0;
  margin-right: 1.236rem;
  height: auto;
  width: 160px;
  overflow: hidden;
}

.nav-list {
  width: 160px;
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
  color: red;
  background-color: #e4e1e122;
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

.nav-item:hover {
  /* 悬停变色 */
  background-color: #fff;
}

.nav-info {
  display: flex;
  overflow: hidden;
  height: 2.3rem;
  width: 100%;
  background-color: #f3f3f2;
  transition: background-color .1s;
}

#content {
  order: 2;  
  position: relative;
  overflow: hidden;
}
</style>
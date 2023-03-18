<template>
  <!-- 把header独立出来放在app.vue里 -->
  <div id="container">
    <header class="header">
      <div><a href="#">Link 1</a></div>
      <div><a href="#">Link 1</a></div>
    </header>

    <main id="main">
      <!-- 目录 -->
      <div class="nav">
        <nav class="nav-list">
          <a class="nav-item">

            <span>111111</span>

          </a>
        </nav>
      </div>
      <!-- 内容 -->
      <div class="content">
        <div class="item-list">
          <div class="item" v-for="(item, index) in items" :key="index">
            <div class="item-content">
              <a href="#" class="item-a">
                <div class="image" style="background-image:url(../../public/1.jpg);">
                </div>
              </a>
              <div class="item-right">
                <div class="item-info">{{ item.name }}</div>
                <div class="item-meta">{{ item.tag }}</div>
              </div>

            </div>

          </div>
        </div>
      </div>
      <!-- 排行 -->
      <div class="rank"></div>
    </main>


    <footer id="footer"></footer>
  </div>
</template>
  

<script lang='ts' setup>
const items = [{ name: 'Item 1', tag: 'red' },
{ name: 'Item 2', tag: 'yellow' },
{ name: 'Item 3', tag: 'blue' },
{ name: 'Item 1', tag: 'red' },
{ name: 'Item 2', tag: 'yellow' },
{ name: 'Item 3', tag: 'blue' },
{ name: 'Item 1', tag: 'red' },
{ name: 'Item 2', tag: 'yellow' },
{ name: 'Item 3', tag: 'blue' },
{ name: 'Item 1', tag: 'red' },
{ name: 'Item 2', tag: 'yellow' },
{ name: 'Item 3', tag: 'blue' }]
</script>

<style scoped>
.header {
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

#container {
  display: flex;
  flex-direction: column;
  /* height: 100%; 添加此属性会导致item-list超出父元素*/
  width: 100%;
  /* 水平居中 */
  justify-content: center;
  /* 竖直居中 */
  align-items: center;
  padding-top: 4.6rem;
  background-color: #e3e3e3;
  /* margin: 0;
  padding: 0; */

}

#main {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 1050px;
}

#footer {
  background-color: #333;
  color: #fff;
  height: 60px;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav {
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
  background-color: #e3e3e3; 
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
  /* padding: 0.6rem; */
  background-color: #f3f3f2;
  transition: background-color .1s;
}

.content {
  order: 2;
  flex-grow: 1;
  position: relative;
  overflow: hidden;
  /* transition: none; */
}

.rank {
  order: 3;
  height: 100px;
  width: 100px;
  display: flex;
  background-color: yellow;
  flex-shrink: 0;
  margin-left: 1.236rem;
}

.item-list {
  box-sizing: inherit;
  border-style: solid;
  border-width: 0;
}

.item {
  margin-bottom: 1.236rem;
  position: relative;
  border-radius: 4px;
  overflow: hidden;

}

.item-content {
  display: flex;
  height: 11rem;
  padding: 0.618rem;
  overflow: hidden;
  background-color: #f3f3f2;
  transition: background-color .1s;
}

.item-content:hover {
  /* 悬停变色 */
  background-color: #fff;
}

.item-a {
  flex-shrink: 0;
  width: 186px;
  height: 9.764rem;
  margin-right: 1.236rem;
  position: relative;
  border-radius: 3px;
  overflow: hidden;
}

.image {
  height: 100%;
  min-width: 100%;
  width: 189px;
  max-width: 189px;
  border-color: transparent;
  background-size: cover;
  background-position: center;
  opacity: 1;
  /* transform: translate(0); 的作用是将元素移动到水平和垂直方向上的位置都不发生变化。这个属性可以用来解决一些布局问题，比如父元素设置了display: flex;后子元素在某些情况下会发生一些意料之外的偏移。
在这种情况下，将子元素的transform属性设置为translate(0)，可以强制浏览器重新计算子元素的位置，从而避免偏移问题。 */
  transform: translate(0);
  transition: transform .25s, opacity .25s;
}

.item-right {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 9.764rem;
  padding-right: 0.309rem;
}

.item-info {
  display: flex;
  height: 11rem;
  padding: 0.618rem;
  overflow: hidden;
}

.item-meta {
  height: 2em;
  line-height: 2em;
  display: flex;
  justify-content: space-between;
  /* 容器中的项目将以其文本基线（text baseline）对齐，而不是默认的盒模型基线（box baseline）。文本基线是所有字形的底部对齐线，而盒模型基线是元素边框的底部线。 */
  align-items: baseline;
  overflow: hidden;
  font-size: 12.432px;
  /* 强制文本在一行内显示，不允许换行。 */
  white-space: nowrap;
  /* 如果文本超出了容器的宽度，使用省略号 ... 表示被截断的文本部分。 */
  text-overflow: ellipsis;
  /* 如果一个单词太长无法放在一行内，就将整个单词移到下一行，不允许将单词拆分成两行。 */
  word-wrap: normal;
}

.item-image {
  height: auto;
  background-color: chartreuse;
}</style>
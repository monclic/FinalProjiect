<template>
  <!-- 把header独立出来放在app.vue里 -->
  <div id="container">
    <header class="header">
      <div><a href="#">Link 1</a></div>
      <div><a href="#">Link 1</a></div>
    </header>

    <main id="main">
      <!-- 目录 -->
      <div class="menu"></div>
      <!-- 内容 -->
      <div class="content">
        <div class="item-list">
          <div class="item" v-for="(item, index) in items" :key="index">
            <div class="item-image">
              <!-- <img :src="" alt=""> -->
            </div>
            <div class="item-info">
              <div class="item-title">{{ item.name }}</div>
              <div class="item-price">{{ item.tag }}</div>
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
  opacity: 0.5;
  border-bottom: 1px solid #f5f5f5;
}

#container {
  display: flex;
  flex-direction: column;
  /* height: 100%; 添加此属性会导致item-list超出父元素*/
  width: 100%;
  background-color: beige;
  /* 水平居中 */
  justify-content: center;
  /* 竖直居中 */
  align-items: center;
  padding-top: 3rem;
  /* margin: 0;
  padding: 0; */

}

#main {
  /* 占满剩余的空间，实现自适应高度的效果 */
  flex: 1;
  background-color: #eee;
  /* padding-top: 50px; */
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 1050px;
  position: relative;
  /* overflow: hidden; */
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

.menu {
  order: 1;
  flex-shrink: 0;
  position: fixed;
  height: 500px;
  width: 100px;
  display: flex;
  background-color: turquoise;
  /* margin-left: 200px; */
  flex-shrink: 0;
  margin-right: 2 .236rem;
}

.content {
  order: 2;
  height: 100%;
  flex: 1;
  display: flex;
  background-color: azure;
}

.rank {
  order: 3;
  height: 100px;
  width: 100px;
  display: flex;
  background-color: blanchedalmond;
  /* margin-right: 200px; */
  flex-shrink: 0;
  margin-left: 1.236rem;
}

.item-list {
  /* margin-top: 10px; */
  display: flex;
  gap: 20px;
  /* flex-direction: column; 属性将 Flex 容器的主轴方向设置为从上到下。因此，子元素会按照从上到下的顺序排列 */
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.item {
  width: 90%;
  height: 150px;
  border: 1px solid #ccc;
  box-sizing: border-box;
  display: flex;
  flex-direction: row;
  background-color: #f5f4f3;
}

.item-info {
  padding: 10px;
}

.item-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.item-price {
  font-size: 14px;
  color: red;
}

.item-image {
  height: auto;
  background-color: chartreuse;
}
</style>
<template>
  <div id="shorts-core">
    <div class="shorts-main">
      <div class="pub-wrapper" v-show="short_publish_show">
        <short_publish></short_publish>
      </div>

      <masonry-wall :items="items" :column-width="350" :gap="1">
        <template #default="{ item, index }">
          <div class="item-wrapper">
            <div class="item-content">
              <div class="item-info">
                <div class="item-title">{{ item.name }}</div>
                <div class="item-meta">{{ item.tag }}</div>
              </div>
              <a href="#" class="item-a">
                <img :src="'../../public/' + item.img">
              </a>
            </div>
          </div>
        </template>
      </masonry-wall>
    </div>
    <div class="shorts-aside">
      <!-- <span style="color:beige"> tag rank publish</span> -->
      <div class="shorts-aside-wrapper">
        <div class="sort">
          <div class="sort_item">综合排序</div>
          <div class="sort_item">最新发布</div>
          <div class="sort_item">最多点赞</div>
          <div class="sort_item">最多回复</div>
        </div>
        <div class="others">
          <div class="others_item" @click="dialogShow">
            <span>点击发布</span>

          </div>
          <div class="others_item">返回顶部</div>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script lang='ts'>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'testVV',
});

</script>
<script lang='ts' setup>
import { ref } from 'vue';
import short_publish from './short-publish.vue'
const short_publish_show=ref(false)
const items = [
  { name: 'Item 1', tag: 'red', img: '1.jpg' },
  { name: 'Item 2', tag: 'yellow', img: '23.png' },
  { name: 'Item 3', tag: 'blue', img: '1.jpg' },
  { name: 'Item 4', tag: 'red', img: '23.png' },
  { name: 'Item 5', tag: 'yellow', img: '23.png' },
  { name: 'Item 6', tag: 'blue', img: '1.jpg' },
  { name: 'Item 7', tag: 'red', img: '1.jpg' },
  { name: 'Item 8', tag: 'yellow', img: '23.png' }];

// const mref = ref<any>();
function dialogShow() {
  short_publish_show.value=!short_publish_show.value
}
</script>

<style scoped>
#shorts-core {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  margin-right: 0;
}

.shorts-main {
  order: 3;
  position: relative;
  flex-grow: 1;
  /* display: flex; 使用masonry-wall不能在这里用这个*/
  overflow: hidden;
}

.shorts-aside {
  order: 4;
  flex-shrink: 0;
  margin-left: 1.236rem;
  height: 300px;
  width: 14.6rem;
  overflow: hidden;
  /* background-color: aqua; */
}

.item-list {
  /* 将一个元素同时设置为行内（inline）和块级（block）元素的特性。它使元素既可以像行内元素一样排列在一行中，又可以像块级元素一样具有宽度、高度、内边距和外边距。 */
  /* display: inline-block; */
  display: flex;
  flex: 0 0 auto;
  width: 50%;
  max-width: 100%;
  padding-right: calc(1rem* .5);
  padding-left: calc(1rem * .5);
  margin-bottom: 1.236rem;
  border-radius: 4px;
  overflow: hidden;
}

.item-wrapper {
  width: 95%;
  border-radius: 4px;
  background-color: #f2f3f4;
  margin-bottom: 1rem;
  padding: 10px 30px 10px;
  overflow: hidden;
}

.item-content {
  /* flex-grow放大比例 flex-shrink缩小 flex-basis初始宽度 */
  flex: 1 1 auto;
  position: relative;
  display: flex;
  flex-direction: column;
  margin-top: var(1rem);
  overflow: hidden;
  /* 水平居中 */
  justify-content: center;
}



.item-a {
  display: flex;
  flex-shrink: 0;
  width: 100%;
  /* height: 12rem; */
  position: relative;
  border-radius: 5px;
  overflow: hidden;
}

.imgs {
  flex: 1
}

img {
  float: left;
  width: auto;
  height: auto;
  max-width: 220px;
  max-height: 220px;
  margin-right: 10px;
  border-radius: 5px;
  margin-top: 4px;
  margin-bottom: 5px;
  margin-right: 10px;
}

.item-info {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-right: 0.309rem;
  z-index: 40;

}

.item-title {
  display: flex;
  padding: 0.618rem;
  overflow: hidden;
}

.item-meta {
  height: 2em;
  padding: 0 0.618rem;
  line-height: 2em;
  display: flex;
  /* justify-content: space-between; */
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

.item-list:hover .item-info {
  bottom: 3rem;
}

.shorts-aside-wrapper {
  position: fixed;
  display: flex;
  flex-direction: column;
}

.sort {
  width: 14rem;
}

.sort_item {
  display: flex;
  align-items: center;
  position: relative;
  width: 100%;
  height: 3em;
  margin-bottom: 0.618rem;
  padding: 0 1.236rem;
  border-radius: 3px;
  overflow: hidden;
  text-decoration: none;
  letter-spacing: .5px;
  user-select: none;
  will-change: background-color;
  transition: background-color .06s;
  background-color: #fff;
}

.others {
  width: 3.3rem;
  margin-top: 260px;
}

.others_item {
  border-radius: 6px;
  height: 50px;
  width: 100%;
  text-align: center;
  padding: 4px 4px;
  display: flex;
  position: relative;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  background-color: #fff;
  margin-bottom: 0.618rem;
  user-select: none;

}

.pub-wrapper {
  padding: 10px 20px;
  margin: 10px 20px 30px 0;
  background-color: #fff;
  border-radius: 4px;
}
</style>
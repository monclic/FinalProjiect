<template>
  <div class="section">
    <div class="section-id">标题{{ message }}</div>
    <div class="section-more">
      <span>查看更多</span>
    </div>
  </div>
  
  <div class="item-list-wrapper">
    <div class="item-list" v-for="(item, index) in items" :key="index">
      <div class="item-content">
        <a href="#" class="item-a">
          <div class="image" :style="{ backgroundImage: 'url(' + item.img + ')' }"></div>
        </a>
        <div class="item-info">
          <div class="item-title">{{ item.name }}</div>
          <div class="item-meta">{{ item.tag }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang='ts'>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'Section',
  props: {
    message: {
      type: String,
      required: true
    }
  },
  setup() {
    const items = [{ name: 'Item 1', tag: 'red' ,img: '1.jpg'},
    { name: 'Item 2', tag: 'yellow' ,img: '1.jpg'},
    { name: 'Item 3', tag: 'blue' ,img: '1.jpg'},
    { name: 'Item 1', tag: 'red' ,img: '1.jpg'},
    { name: 'Item 2', tag: 'yellow' ,img: '1.jpg'},
    { name: 'Item 3', tag: 'blue' ,img: '1.jpg'},
    { name: 'Item 1', tag: 'red' ,img: '1.jpg'},
    { name: 'Item 2', tag: 'yellow' ,img: '23.png'}];
    return {
      items
    };
  }
});

</script>

<style scoped>
.section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.8rem;
  padding-right: 50px;
}

.section-id {
  font-size: 1.61rem;
  font-weight: bold;
  color: #0088f5;
}

.section-id:hover {
  color: #0088f5;
}

.section-more {
  height: 2.7rem;
  width: 7.7rem;
  box-sizing: inherit;
  /* border-width border-style border-color */
  border: 0 solid;
  border-radius: 10px;
  display: flex;
  /* 水平居中 */
  justify-content: center;
  /* 竖直居中 */
  align-items: center;

  transition: background-color 0.2s;

}

.section-more span {
  font-size: 1.1rem;
  font-weight: 900;
  color: #0088f5;
  transition: color 0.2s;
}

.section-more:hover {
  background-color: #0088f5;
}

.section-more:hover span {
  color: #f4f3f3f9;
}

/* --- */
.item-list-wrapper {
  display: flex;
  flex-wrap: wrap;
  margin: 0;
}

.item-list {
  /* 将一个元素同时设置为行内（inline）和块级（block）元素的特性。它使元素既可以像行内元素一样排列在一行中，又可以像块级元素一样具有宽度、高度、内边距和外边距。 */
  /* display: inline-block; */
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



.item-content {
  /* flex-grow放大比例 flex-shrink缩小 flex-basis初始宽度 */
  flex: 1 1 auto;
  position: relative;
  display: flex;
  flex-direction: column;  
  margin-top: var(1rem);  
  overflow: hidden;
  /* background-color: #f3f3f2; */
  transition: background-color .1s;
  /* 水平居中 */
  justify-content: center;
 /* 悬停后浮起 */
 transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

/* 悬停后浮起 */
.item-content:hover {
  /* 浮起8px */
  transform: translateY(-8px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}
.item-a {
  position: absolute;
  flex-shrink: 0;
  width: 100%;
  height: 12rem;
  position: relative;
  border-radius: 5px;
  overflow: hidden;
}

.image {
  height: 100%;
  width: 100%;
  border-color: transparent;
  background-size: cover;
  background-position: center;
  opacity: 1;
  /* transform: translate(0); 的作用是将元素移动到水平和垂直方向上的位置都不发生变化。这个属性可以用来解决一些布局问题，比如父元素设置了display: flex;后子元素在某些情况下会发生一些意料之外的偏移。
在这种情况下，将子元素的transform属性设置为translate(0)，可以强制浏览器重新计算子元素的位置，从而避免偏移问题。 */
  transform: translate(0);
  transition: transform .25s, opacity .25s;
  z-index: 20;
}

.image::after {
  pointer-events: none;
  content: "";
  position: absolute;
  z-index: 35;
  top: 13%;
  left: 0;
  height: 120%;
  width: 100%;
  background: linear-gradient(180deg, rgba(243, 243, 242, 0) 0, rgba(243, 243, 242, 0.013) 8.1%, rgba(243, 243, 242, 0.049) 15.5%, rgba(243, 243, 242, 0.104) 22.5%, rgba(243, 243, 242, 0.175) 29%, rgba(243, 243, 242, 0.259) 35.3%, rgba(243, 243, 242, 0.352) 41.2%, rgba(243, 243, 242, 0.45) 47.1%, rgba(243, 243, 242, 0.55) 52.9%, rgba(243, 243, 242, 0.648) 58.8%, rgba(243, 243, 242, 0.741) 64.7%, rgba(243, 243, 242, 0.825) 71%, rgba(243, 243, 242, 0.896) 77.5%, rgba(243, 243, 242, 1) 84.5%, rgba(243, 243, 242, 1) 91.9%, #e7e7e7);
}

.item-info {
  position: absolute;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-right: 0.309rem;
  bottom: 1rem;
  z-index: 40;
  transition: bottom 0.3s ease;

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
}</style>
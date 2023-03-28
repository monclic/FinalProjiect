<template>
    <div class="container">
      <div class="nav">
        <ul>
          <li v-for="(item, index) in navList" :key="index" :class="{ active: index === activeIndex }" @click="handleClick(index)">{{ item }}</li>
        </ul>
      </div>
      <div class="content" ref="content" @scroll="handleScroll">
        <div v-for="(item, index) in contentList" :key="index" :ref="'content_' + index">{{ item }}</div>
      </div>
    </div>
  </template>
  
  <script lang="ts" setup>
  import { ref, watch, onBeforeUnmount } from 'vue';
  
  const navList = ref(['Section 1', 'Section 2', 'Section 3', 'Section 4']);
  const contentList = ref(['Section 1 Content', 'Section 2 Content', 'Section 3 Content', 'Section 4 Content']);
  
  let activeIndex = 0;
  
  const handleClick = (index: number) => {
    activeIndex = index;
    const content = document.querySelector('.content');
    const contentList = document.querySelectorAll('.content > div');
    const targetContent = contentList[index];
    content.scrollTo({
      top: targetContent.offsetTop,
      behavior: 'smooth',
    });
  };
  
  const handleScroll = () => {
    const content = document.querySelector('.content');
    const contentList = document.querySelectorAll('.content > div');
    let closestIndex = 0;
    let closestDistance = Infinity;
    contentList.forEach((item, index) => {
      const distance = Math.abs(item.offsetTop - content.scrollTop);
      if (distance < closestDistance) {
        closestDistance = distance;
        closestIndex = index;
      }
    });
    activeIndex = closestIndex;
  };
  
  watch(navList, () => {
    activeIndex = 0;
  });
  
  onBeforeUnmount(() => {
    window.removeEventListener('scroll', handleScroll);
  });
  
  window.addEventListener('scroll', handleScroll);
  
  const containerStyle = {
    display: 'flex',
  };
  
  const navStyle = {
    position: 'fixed',
    top: 0,
    left: 0,
    backgroundColor: '#eee',
    width: '200px',
    height: '100%',
  };
  
  const navActiveStyle = {
    backgroundColor: '#ccc',
  };
  
  const contentStyle = {
    marginLeft: '200px',
    height: '100%',
    overflow: 'auto',
  };
  
  </script>
  
  <style scoped>
  .container {
    display: flex;
  }
  
  .nav {
    position: fixed;
    top: 0;
    left: 0;
    background-color: #eee;
    width: 200px;
    height: 100%;
  }
  
  .nav ul {
    list-style: none;
    padding: 0;
    margin: 0;
  }
  
  .nav li {
    padding: 10px;
    cursor: pointer;
  }
  
  .nav li.active {
    background-color: #ccc;
  }
  
  .content {
    margin-left: 200px;
    height: 100%;
    overflow: auto;
  }
  </style>
  
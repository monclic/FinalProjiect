<template>
  <div id="container">
    <main id="main">

      <!-- content -->
      <div id="content">
        <!-- for  -->
        <!-- div.item  -->
        <!-- template  -->
        <!-- /for -->
        <!-- <div v-for="(message,index) in messages" :key="index" :id="message" class="section-wrapper">
        <Section :message="message"/>
      </div>        -->
      <router-view></router-view>
        </div>

     
    </main>

    <footer id="footer"></footer>
  </div>
</template>

<script lang='ts' setup>
import Section from './section.vue'
import Nav from './nav.vue'
import shorts from './Short/shorts.vue'

const messages=['one','two','three','four','five']
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
  height: 3.83rem;
  width: 100%;
  /* 固定 */
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  padding: 10px 300px 10px;
  background-color: rgba(255, 255, 255, 0.5); /* 白色，透明度 50% */
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* 置于最前 */
  z-index: 999;
  /* 透明度 */
  /* opacity: 0.5; */
  border-bottom: 1px solid #dedede;
  /* 模糊化  ！！使用此效果不能使用opacity，只能设置background-color为半透明色 */
    backdrop-filter: blur(5px);
}

#main {
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  padding: 0 100px;
}



#content {
  order: 2;  
  position: relative;
  overflow: hidden;
  flex-grow: 1;
}
</style>
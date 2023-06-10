<template>
  <div id="app">
    <!-- <header id="header">
      <div @click="getName">FIRST</div>
      <div class="header-item" @click="to">
        {{ name_space }} -->
        <!-- <div class="name_utils">
          <div class="item">
            <div class="to_info" @click="to_info" style="padding-bottom: 5px;">个人信息</div>
            <div class="form__separator-line"></div>
            <div class="loginout" @click="loginout" style="padding-top: 5px;">登出</div>
          </div>
        </div> -->
      <!-- </div> -->
      <!-- <router-link class="header-item" to="/login">{{ name_space }}</router-link> -->
    <!-- </header> -->
    <router-view></router-view>
  </div>
</template>

<script lang='ts' setup>
import { ref } from 'vue'
import http from './utils/http'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router=useRouter()
const name_space=ref('')

const to=()=>{
  const token=localStorage.getItem('token')
  if(token)return
  router.push('/login')
}

const getName=()=>{
  
  http.get('/user/getName')
  .then((data:any)=>{
    name_space.value=data.goods
    
  })
  .catch(error=>{
    name_space.value='登录/注册'
  }
  )
}


const to_info=()=>{
  router.push('/info')
}

const loginout=()=>{
http.post('/user/LoginOut')
.then((data)=>{
  localStorage.removeItem('token')
}
)
}

onMounted(()=>{
  getName()
})

</script>

<style>
/* :root 是 CSS 中的一个伪类选择器，用于匹配文档的根元素。在 HTML 中，根元素通常指的是 html 元素。使用 :root 可以方便地设置全局的 CSS 变量和字体大小等样式。 */
:root,
html {
  font-size: 16px;
  box-sizing: border-box;
  cursor: default;
  background-color: #e7e7e7;
}

*,
:before,
:after {
  box-sizing: inherit;
  border-style: solid;
  border-width: 0;
}

/* #app {
  height: 100vh;
  width: 100%;
  margin: 0;
  padding: 0;

} */

* {
  margin: 0;
  padding: 0;
  /* 去除表单元素（input、textarea等）默认的外边框 */
  outline: none;
  /* 去除元素的阴影效果 */
  box-shadow: none;
  border-radius: 0;
  /* 继承父元素的字体大小属性 */
  font-size: inherit;
  line-height: inherit;
  background-repeat: no-repeat;
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
.header-item{
  text-decoration: none;
  cursor: pointer;
  color: #61625b;
}
/* .header-item:hover .name_utils{
  display: block;
} */
.name_utils{
  background-color: #fff;
    border: 1px solid #e3e5e7;
    color: #222;
    font-family: PingFang SC, HarmonyOS_Regular, Helvetica Neue, Microsoft YaHei, sans-serif !important;
    margin-bottom: 10px;
    margin-top: 7px;
    pointer-events: all;
    position: absolute;    
    background-color: #fff;
    display: flex;
    flex-direction: column;
    border-radius: 7px;
}
.item{
  text-align: center;
  padding: 10px;
}
.form__separator-line {
    display: block;
    /* width: 100%; */
    height: 0;
    border-bottom: 1px solid #e3e5e7;
  }
</style>
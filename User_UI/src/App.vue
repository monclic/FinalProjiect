<template>
  <div id="app">
    <!-- <header id="header">
      <div><a href="#">Link 1</a></div>
      <div @click="login" style="cursor: pointer;">Login</div>
    </header> -->
    <router-view></router-view>
  </div>
</template>

<script lang='ts' setup>
import http from './utils/http'

const login=()=>{
  const formData= new FormData()
  formData.append('email','111@1.com')
  formData.append('pwd','111111')
  let params={email:'111@1.com',pwd:'111111'}

http.post('/user/AccountLogin',params)
.then(data=>{
  localStorage.setItem('token',data.token)
})

  
}
</script>

<style>
/* :root 是 CSS 中的一个伪类选择器，用于匹配文档的根元素。在 HTML 中，根元素通常指的是 html 元素。使用 :root 可以方便地设置全局的 CSS 变量和字体大小等样式。 */
:root,
html {
  font-size: 16px;
  box-sizing: border-box;
  cursor: default;
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
</style>
<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script lang='ts' setup>

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
}</style>
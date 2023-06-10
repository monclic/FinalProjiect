<template>
  <div id="container">
    <header id="header">
      <div @click="getName">FIRST</div>
      <div v-if="header_show"  @click="to" class="header-item1">
        登录/注册
      </div>
      <div v-else class="header-item">
        {{ name_space }}
        <div class="name_utils">
          <div class="item">
            <div class="to_info" @click="to_info" style="padding-bottom: 5px;">个人信息</div>
            <div class="form__separator-line"></div>
            <div class="loginout" @click="loginout" style="padding-top: 5px;">登出</div>
          </div>
        </div>
      </div>
      <!-- <router-link class="header-item" to="/login">{{ name_space }}</router-link> -->
    </header>
    <main id="main">
      <!-- nav -->
      <div id="nav">
        <Nav/>
      </div>
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
import Nav from './nav.vue'
import { ref } from 'vue'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import http from '../utils/http'

const messages=['one','two','three','four','five']


const router=useRouter()
const name_space=ref('')

const to=()=>{
  const token=localStorage.getItem('token')
  if(token)return
  router.push('/login')
}

const header_show=ref(true)
const getName=()=>{
  
  http.get('/user/getName')
  .then((data:any)=>{
    name_space.value=data.goods
    header_show.value=false
  })
  .catch(error=>{
    header_show.value=true
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

#nav {
  order: 1;
  flex-shrink: 0;
  margin-right: 1.236rem;
  height: auto;
  width: 140px;
  overflow: hidden;
}



#content {
  order: 2;  
  position: relative;
  overflow: hidden;
  flex-grow: 1;
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
.header-item1{
  text-decoration: none;
  cursor: pointer;
  color: #61625b;
  height: 100%;
  padding: 10px;
}
.header-item{
  text-decoration: none;
  cursor: pointer;
  color: #61625b;
  height: 100%;
  padding: 10px;
}
.header-item:hover .name_utils{
  display: block !important ;
}
.name_utils{
  display: none;
}
</style>
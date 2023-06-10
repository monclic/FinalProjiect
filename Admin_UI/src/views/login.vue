<template>
    <div class="login-container">
      <div class="login-wrapper">
        <div class="login-box">
          <div class="header">
            <div class="login">
              <div class="title">登录</div>
            </div>
  
          </div>
          <div class="login-body">
            <!-- 登录区 -->
            <div v-show="isLogin" class="login-space">
              <form class="tab__form">
                <div class="form__item">
                  <div class="form_info">账号</div>
                  <input v-model="email" autocomplete="on" maxlength="32"
                    oninput="value=value.replace(/\s+/g, '')" placeholder="请输入账号" type="text">
                </div>
                <div class="form__separator-line"></div>
                <div class="form__item">
                  <div class="form_info">密码</div>
                  <input v-model="pwd" autocomplete="on" maxlength="32"
                    oninput="value=value.replace(/\s+/g, '')" placeholder="请输入密码" type="password">
                </div>
              </form>
              <div   class="btn_wp" style="justify-content: center;"><!---->
                <div @click="login" class="btn_primary"> 登录 </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script lang='ts' setup>
  import { ref } from 'vue';
  import http from '../utils/http';
  import { useRouter } from 'vue-router';

  
  const isLogin=ref(true)
  const switchSpace=(type:boolean)=>{
  
    isLogin.value=type
  }
  const email=ref()
  const pwd=ref('')
  const code=ref('')
  
  const router = useRouter();
 

  const login=()=>{
    
    if(email.value==''|| pwd.value=='') return
    let params={id:email.value,pwd:pwd.value}
  http.post('/admin/AccountLogin',params)
  .then((data:any)=>{  
    localStorage.setItem('token_admin',data.token)   

  
    router.push('/board')
  })
  }
 


  </script>
  
  <style scoped>
  .login-container {
    background-color: #e7e7e7;
    height: 100vh;
    width: 100%;
    display: flex;
    flex-grow: 1;
    border-radius: 6px;
    padding: 10px;
    /* border: 3px solid rgba(255, 255, 255, 0.5); */
  }
  
  .login-wrapper {
    margin: auto;
    height: 350px;
    /* width: 300px; */
    border-radius: 6px;
    border: 10px solid rgba(255, 255, 255, 0.5);
  }
  
  .login-box {
    background-color: #fff;
    height: 100%;
    display: flex;
    flex-direction: column;
  }
  
  .header {
    display: flex;
    flex-direction: row;
    justify-content: center;
    padding-top: 20px;
  }
  
  .login-body {
    margin-top: 20px;
    padding: 20px 20px;
  }
  
  .login-space {
    position: relative;
  }
  
  .tab__form {
    width: 400px;
    /* height: 90px; */
    border: 1px solid #e3e5e7;
    border-radius: 8px;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 20px;
    color: #212121;
    padding: 0 10px;
  }
  
  .tab__form .form__item {
    position: relative;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-pack: start;
    -ms-flex-pack: start;
    justify-content: flex-start;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 100%;
    height: 44px;
  }
  
  .tab__form .form__item input {
    -webkit-box-flex: 1;
    -ms-flex: 1;
    flex: 1;
    min-width: 0;
    -webkit-box-shadow: 0 0 0 1000px #fff inset;
    box-shadow: inset 0 0 0 1000px #fff;
    margin-left: 20px;
  }
  
  .tab__form .form__separator-line {
    display: block;
    width: 100%;
    height: 0;
    border-bottom: 1px solid #e3e5e7;
  }
  
  .eye-btn,
  .eye-btn svg {
    width: 20px;
    height: 20px;
  }
  
  .eye-btn{
    position: relative;
    display: inline-block;
    cursor: pointer;
    margin-right: 10px;
  }
  
  .eye-btn svg {
    position: absolute;
    top: 0;
    left: 0;
  }
  
  .clickable {
    color: #00a1d6;
    cursor: pointer;
  }
  
  .btn_wp {
    display: flex;
    margin-top: 20px;
    width: 400px;
    font-style: normal;
    font-weight: 400;
    font-size: 14px;
    line-height: 40px;
    text-align: center;
  }
  
  .btn_primary {
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    width: 194px;
    height: 40px;
    cursor: pointer;
    color: #fff;
    background: #00aeec;
    border-radius: 8px;
  }
  .login-sms-wp__vertical-line {
      width: 1px;
      height: 26px;
      border-left: 1px solid #e3e5e7;
      margin-right: 20px;
  }
  .register{
    position: relative;
  }
  .disable {
      color: #c9ccd0!important;
      cursor: not-allowed;
  }
  .login-sms-send {
      width: 90px;
      text-align: center;
  }
  .btn_primary:hover {
      opacity: .85;
  }
  .title{
   text-decoration: none;color: #000;
  
   user-select: none;
  }
  .actived{
    cursor: not-allowed;
    color: #000;
  }
  </style>
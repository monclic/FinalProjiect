<template>
    <div class="login-container">
        <div class="login-wrapper">
            <div class="login-box">
                <div class="header">
                    <div class="login">
                        <div class="title">找回密码</div>
                    </div>
                </div>
                <div class="login-body">
                    <!-- 注册区 -->
                    <div class="register">
                        <form class="tab__form">
                            <div class="form__item">
                                <div>email</div>
                                <input v-model="email" maxlength="30" oninput="value=value.replace(/\s+/g, '')"
                                    @input="checkEmail" placeholder="请输入email">
                                <div class="login-sms-wp__vertical-line"></div>
                                <div @click="getCode"
                                    :class="{ 'login-sms-send': true, 'clickable': true, 'disable': !email_is || countdown > 0 }">
                                    {{ countdown > 0 ? countdown + '秒' : buttonText }}
                                </div>
                            </div>
                            <div class="form__separator-line"></div>
                            <div class="form__item">
                                <div>验证码</div>
                                <input v-model="code" placeholder="请输入验证码" maxlength="6"
                                    oninput="value=value.replace(/[^\d]/g, '')">
                            </div>
                            <div class="form__separator-line"></div>
                            <div class="form__item">
                                <div class="form_info">密码</div>
                                <input v-model="pwd" autocomplete="on" maxlength="32"
                                    oninput="value=value.replace(/\s+/g, '')" placeholder="请输入新的密码">
                            </div>
                            <div class="form__separator-line"></div>
                            <div class="form__item">
                                <div class="form_info">密码</div>
                                <input v-model="pwd_verify" autocomplete="on" maxlength="32"
                                    oninput="value=value.replace(/\s+/g, '')" placeholder="请确认密码">
                            </div>
                        </form>
                        <div class="btn_wp" style="justify-content: center;"><!---->
                            <div @click="register" class="btn_primary "> 确定 </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script lang='ts' setup>
import { ref } from 'vue';
import http from '../../utils/http';
import { useRouter } from 'vue-router';

const isLogin = ref(true)
const switchSpace = (type: boolean) => {

    isLogin.value = type
}
const email = ref('1727263980@qq.com')
const pwd = ref('')
const code = ref('')

const router = useRouter();


const login = () => {

    if (email.value == '' || pwd.value == '') return
    let params = { email: email.value, pwd: pwd.value }
    http.post('/user/AccountLogin', params)
        .then((data: any) => {
            localStorage.setItem('token', data.token)

            // router.push('/')
        })
}

const email_is = ref(false)
const checkEmail = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 正则表达式检查邮箱形式

    if (emailRegex.test(email.value)) {
        email_is.value = true;
    } else {
        email_is.value = false;
    }
};


const countdown = ref(0);
const buttonText = ref('获取验证码');
let timer: ReturnType<typeof setTimeout> | null = null;

const getCode = () => {
    if (!email_is.value) return

    code_post()

    // 页面显示
    if (countdown.value === 0) {
        countdown.value = 60;
        buttonText.value = countdown.value + '秒';

        timer = setInterval(() => {
            countdown.value--;

            if (countdown.value === 0) {
                clearInterval(timer!);
                timer = null;
                buttonText.value = '重新发送';
            } else {
                buttonText.value = countdown.value + '秒';
            }
        }, 1000);
    }



}
const code_post = async () => {
    // 发送请求
    let uemail: any = email.value
    let param = { email: email.value }

    http.post('/user/sendCode_2', email.value, {
        headers: {
            'Content-Type': 'text/plain' // 指定请求体的内容类型为纯文本
        }
    })
}

const pwd_verify = ref('')
const register = () => {
    if (email.value == '' || pwd.value == '' || code.value == '' || pwd_verify.value == '') return
    if (pwd.value != pwd_verify.value) return
    let params = { email: email.value, pwd: pwd.value, code: code.value }
    http.post('/user/ReSetPwd', params)
        .then((data: any) => {
            localStorage.setItem('token', data.token)
            router.replace('/login')
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

.eye-btn {
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

.register {
    position: relative;
}

.disable {
    color: #c9ccd0 !important;
    cursor: not-allowed;
}

.login-sms-send {
    width: 90px;
    text-align: center;
}

.btn_primary:hover {
    opacity: .85;
}

.title {
    text-decoration: none;
    color: #000;
    cursor: pointer;
    user-select: none;
    cursor: not-allowed;
}

</style>
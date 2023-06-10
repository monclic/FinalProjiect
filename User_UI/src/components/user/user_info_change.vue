<template>
    <div class="space-container">
        <div class="item">
            <div class="item-wrapper">
                <div class="tip">用户头像</div>
                <div class="avatar" :style="{ backgroundImage: 'url(' + avatar + ')' }">
                </div>
            </div>
            <div class="button" @click="el_dialog_show(1)"> 修改头像 </div>
        </div>
        <div class="item">
            <div class="item-wrapper">
                <div class="tip">用户昵称</div>
                <div class="tip">{{ username }}</div>
            </div>
            <div class="button" @click="el_dialog_show(2)">修改昵称</div>
        </div>
        <div class="item">
            <div class="item-wrapper">
                <div class="tip">绑定邮箱</div>
                <div class="tip">{{ email }}</div>
            </div>
            <div class="button" @click="el_dialog_show(3)">修改邮箱</div>
        </div>
        <div class="item">
            <div class="item-wrapper">
                <div class="tip">用户密码</div>
                <div class="tip">已加密</div>
            </div>
            <div class="button" @click="el_dialog_show(4)">修改密码</div>
        </div>
        <div class="item">
            <div class="item-wrapper">
                <div class="tip">uid</div>
                <div class="tip">{{ uid }}</div>
            </div>
        </div>
        
<!-- 更新头像 -->
        <el-dialog v-model="centerDialogVisible1" title="" width="30%" center>
            <div class="avatar_change">               
                <div class="dialog_avatar" :style="{ backgroundImage: 'url(' + img + ')' }">                    
                </div>
                <div class="upload" @click="uploadImage">选择图片</div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="centerDialogVisible1 = false">取消</el-button>
                    <el-button type="primary" @click="change_avatar">
                        确认
                    </el-button>
                </span>
            </template>
        </el-dialog>        
<!-- 更新昵称 -->
        <el-dialog v-model="centerDialogVisible2" title="" width="30%" center>
            <div class="dialogbody">
                <span> 更新昵称 </span>
                <input v-model="username_new" autocomplete="on" maxlength="32" placeholder="请输入新昵称" type="text">
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="centerDialogVisible2 = false">取消</el-button>
                    <el-button type="primary" @click="change_username">
                        确认
                    </el-button>
                </span>
            </template>
        </el-dialog>
<!-- 更新邮箱 -->
        <el-dialog v-model="centerDialogVisible3" title="" width="30%" center>
            <form class="tab__form">
                <!-- 旧邮箱 -->
                <div class="form__item">
                    <div>email</div>
                    <input v-model="email_old" maxlength="30" oninput="value=value.replace(/\s+/g, '')" @input="checkEmail"
                        placeholder="请输入email">
                    <div class="login-sms-wp__vertical-line"></div>
                    <div @click="getCode(1)"
                        :class="{ 'login-sms-send': true, 'clickable': true, 'disable': !email_is }">
                        {{ buttonText }}
                    </div>
                </div>
                <div class="form__separator-line"></div>
                <div class="form__item">
                    <div>验证码</div>
                    <input v-model="code" placeholder="请输入验证码" maxlength="6" oninput="value=value.replace(/[^\d]/g, '')">
                </div>

                <!-- 新邮箱 -->
                <div class="form__item">
                    <div>email</div>
                    <input v-model="email_new" maxlength="30" oninput="value=value.replace(/\s+/g, '')" @input="checkEmail_new"
                        placeholder="请输入email">
                    <div class="login-sms-wp__vertical-line"></div>
                    <div @click="getCode(2)"
                        :class="{ 'login-sms-send': true, 'clickable': true, 'disable': !email_new_is}">
                        {{ buttonText }}
                    </div>
                </div>
                <div class="form__separator-line"></div>
                <div class="form__item">
                    <div>验证码</div>
                    <input v-model="code_new" placeholder="请输入验证码" maxlength="6" oninput="value=value.replace(/[^\d]/g, '')">
                </div>
            </form>                        
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="centerDialogVisible3 = false">取消</el-button>
                    <el-button type="primary" @click="change_email" >
                        确认
                    </el-button>
                </span>
            </template>
        </el-dialog>
<!-- 更新密码 -->
        <el-dialog v-model="centerDialogVisible4" title="" width="30%" center>
            <div class="dialogbody">
                <span> 旧密码 </span>
                <input v-model="pwd_old" autocomplete="on" maxlength="32" placeholder="请输入旧密码" type="text">
            </div>
            <div class="dialogbody">
                <span> 新密码 </span>
                <input v-model="pwd_new" autocomplete="on" maxlength="32" placeholder="请输入新密码" type="text">
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="centerDialogVisible4 = false">取消</el-button>
                    <el-button type="primary" @click="change_pwd">
                        确认
                    </el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script lang='ts' setup>
import { onMounted, ref } from 'vue';
import http from '../../utils/http';
const avatar=ref('')
const username=ref('')
const email=ref('')
const uid=ref('')

const username_new = ref('')
const centerDialogVisible1 = ref(false)
const centerDialogVisible2 = ref(false)
const centerDialogVisible3 = ref(false)
const centerDialogVisible4 = ref(false)
const confirm_ype = ref(-1) // 1-avatar 2-username 3-email 4-pwd

const el_dialog_show = (type: number) => {
    if (type == 1) {
        centerDialogVisible1.value = true
    } else if (type == 2) {
        centerDialogVisible2.value = true
    } else if (type == 3) {
        centerDialogVisible3.value = true
    } else if (type == 4) {
        centerDialogVisible4.value = true
    }


    confirm_ype.value = type
}


const change_username = () => {
    http.post('/user/changeUserName', username_new.value, {
        headers: {
            'Content-Type': 'text/plain' // 指定请求体的内容类型为纯文本
        }
    })

    centerDialogVisible2.value = false
}
// pwd
const pwd_old = ref('')
const pwd_new = ref('')
const change_email_step = ref(true)
const change_pwd = () => {
    let params = { oldPwd: pwd_old.value, newPwd: pwd_new.value }
    http.post('/user/changePwd', params)

    centerDialogVisible4.value = false
}
// email
const email_old = ref('1727263980@qq.com')
const email_new=ref('')
const code = ref('')
const code_new=ref('')
const countdown = ref(0);
const buttonText = ref('获取验证码');
let timer: ReturnType<typeof setTimeout> | null = null;

const email_is = ref(false)
const email_new_is = ref(false)
const checkEmail = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 正则表达式检查邮箱形式
  
    if (emailRegex.test(email_old.value)) {
      email_is.value = true;
    } else {
      email_is.value = false;
    }
  };

  const checkEmail_new = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 正则表达式检查邮箱形式
  
    if (emailRegex.test(email_old.value)) {
        email_new_is.value = true;
    } else {
        email_new_is.value = false;
    }
  };

const getCode = (type:number) => {
    if (!email_is.value) return

    if(type==1)
    code_post()
    else if(type==2){
        code_post_2()
    }

    // 页面显示
    if (countdown.value === 0) {
        countdown.value = 60;

        timer = setInterval(() => {
            countdown.value--;

            if (countdown.value === 0) {
                clearInterval(timer!);
                timer = null;
                buttonText.value = '获取验证码';
            } 
        }, 1000);
    }



}
const code_post = async () => {
    // 发送请求
    http.post('/user/sendCode_changeEmail', email_old.value, {
        headers: {
            'Content-Type': 'text/plain' // 指定请求体的内容类型为纯文本
        }
    }).then((data: any) => {
        change_email_step.value = false
    })
}
const code_post_2 = async () => {
    // 发送请求
    http.post('/user/sendCode_changeEmail_new', email_new.value, {
        headers: {
            'Content-Type': 'text/plain' // 指定请求体的内容类型为纯文本
        }
    }).then((data: any) => {
        change_email_step.value = false
    })
}
const change_email=()=>{
    let params={oldEmail:email_old.value,newEmail:email_new.value,oldCode:code.value,newCode:code_new.value}
    http.post('/user/changeEmail',params)
    .then((data:any)=>{
        change_email_step.value = false
    })
}
// 图片上传
const img=ref('')
const file=ref()
function uploadImage() {
    // 打开文件
    window.showOpenFilePicker({
        types: [{
            accept: {
                'image/*': ['.png', '.gif', '.jpeg', '.jpg', '.webp']
            }
        }],
        // 不可以选择多个图片
        multiple: false
    }).then(async (arrFileHandle: any) => {
        // 遍历选择的文件
        for (const fileHandle of arrFileHandle) {
            // 获取文件内容
            const fileData = await fileHandle.getFile()

            file.value=fileData
            // 创建一个 Image 对象
            const image = new Image();

            // 设置 Image 对象的 src 为选择的文件的 URL，这样它就会加载图片数据
            image.src = URL.createObjectURL(fileData);

            // 等待图片加载完成
            image.onload = function () {
                // 创建一个 Canvas 对象
                const canvas = document.createElement('canvas');
                canvas.width = image.width;
                canvas.height = image.height;

                // 在 Canvas 上绘制图片
                const context = canvas.getContext('2d');
                if (context) {
                    context.drawImage(image, 0, 0);
                }

                // 获取图片的 base64 数据 URI
                const dataURI = canvas.toDataURL('image/jpeg');

                img.value=dataURI                
                
            }
        }
    })
}

const change_avatar=()=>{
    const formData = new FormData();
formData.append('file', file.value); // 假设 fileData 是上传的文件数据

    http.post('/user/upload',formData,{
        headers: {
            'Content-Type': 'multipart/form-data' // 指定请求体的内容类型为 multipart/form-data
        }
    })
}

// 获得信息
const getInfo=()=>{
    http.get('/user/info')
    .then((data:any)=>{
        avatar.value=data.avatar
        username.value=data.username
        email.value=data.email
        uid.value=data.uid        
        
        img.value=data.avatar
        email_old.value=data.email
    })
}

onMounted(() => {
    getInfo()
})
</script>

<style scoped>
.space-container {
    width: 100%;
    display: flex;
    flex-direction: column;
    padding: 0 30px;
    background-color: #fff;
}

.item {
    margin-top: 10px;
    justify-content: space-between;
    /* height: 50px; */
    display: flex;
    flex-direction: row;
}

.item-wrapper {
    display: flex;
    flex-direction: row;
}

.button {
    border-radius: 5px;
    height: 50px;
    width: 100px;
    background-color: rgba(175, 179, 179, 0.579);
    display: flex;
    align-items: center;
    justify-content: center;
    user-select: none;
    cursor: pointer;
    transition: background-color -0.2s;
}

.button:hover {
    background-color: #ccc;
}

.tip {
    width: 100px;
    font-weight: 200;
    display: flex;
    align-items: center;
}

.avatar {
    width: 70px;
    height: 70px;
    border-radius: 50%;
    background-color: #ccc;
    background-size: cover;
}

.avatar:hover {}

.dialogbody {
    display: flex;
    justify-content: center;
}

.dialogbody span {
    font-weight: 700;
    margin-right: 10px;
    display: flex;
    align-items: center;
}

input {
    height: 40px;
    border: 1px solid #ccc;
    border-radius: 5px;
    padding-left: 5px;
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

.clickable {
    color: #00a1d6;
    cursor: pointer;
}


.login-sms-wp__vertical-line {
    width: 1px;
    height: 26px;
    border-left: 1px solid #e3e5e7;
    margin-right: 20px;
}

.disable {
    color: #c9ccd0 !important;
    cursor: not-allowed;
}

.login-sms-send {
    width: 90px;
    text-align: center;
}
.avatar_change{
    display: flex;
    flex-direction: column;
    align-items: center;

}
.dialog_avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    background-color: #ccc;
    background-size: cover;
}
.upload{
    margin-top: 10px;
    color: #fff;
    background-color: #00a1d6;
    height: 30px;
    width: 90px;    
    text-align: center;
    padding-top: 3px;
    border-radius: 5px;
}

</style>
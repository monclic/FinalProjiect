<template>
<div class="admin_info">
  <div class="title_admin">
    <div @click="setA" :class="{'title_item':true,'actived':isActived}">111</div>
    <div style="width: 1px;    border-left: 3px solid #E3E5E7;"></div>
    <div @click="setA" :class="{'title_item':true,'actived':!isActived}">222</div>
  </div>
  <div v-if="isActived" class="admin_body">
    <div v-for="(item,index) in users" class="list_user">
    <div class="item_user">
      <!-- <div class="avatar"></div> -->
      <div class="username">111111</div>
      <div class="button">管理</div>
    </div>
    </div>
  </div>
  <div v-else class="admin-add">
    <div class="form">
      <div class="from_item">
        <span>管理员名：</span>
        <el-input v-model="adminName" placeholder="Please input" />
      </div>
      <div class="from_item">
        <span>管理员密码：</span>
        <el-input v-model="pwd" placeholder="Please input" />
      </div>
      <div class="button">添加</div>
    </div>
  </div>
</div>
</template>

<script lang='ts' setup>
import { ref } from 'vue';

const isActived=ref(true)

const setA=()=>{
  isActived.value=!isActived.value
}

const users=ref([1,2,3,4,5,6,7,8])
const adminName=ref('')
const pwd=ref('')
</script >

<style scoped>
.admin_info{
  width: 100%;
}
.title_admin{
  width: 100%;
  border-radius: 10px;
  padding: 0 100px;
  display: flex;
  flex-direction: row;
}
.title_item{
  width: 50%;
  height: 30px;
  background-color: #fff;
  text-align: center;
  color: #000;
  padding-top: 5px;
}
.actived{
  background-color: rgb(177, 182, 187);
}
.admin_body{
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
}
.list_user{
  width: 25%;
  padding: 5px 5px;
}
.item_user{
  width: 100%;
  height: 100px;
  background-color: #fff;
  display: flex;
  flex-direction: row;
  padding: 5px 5px;
    align-items: center;
}
.avatar{
  height: 70px;
  width: 70px;
  border-radius: 50%;
  background-color: aqua;
  margin-right: 10px;

}
.username{
  width: 50px;
  font-weight: 400;
}
.button{
  height: 40px;
  width: 50px;
  background-color: #2faee3;
  margin-left: 100px;
  border-radius: 10px;
  text-align: center;
  padding-top: 5px;
  color: #fff;
}
.admin-add{
  width: 100%;
}
.form{
  display: flex;
  flex-direction: column;
  align-items: center;
}
.from_item{
  margin-top: 20px;
  display: flex;
  flex-direction: row;
  width: 500px;
}
.from_item span{
  width: 150px;
}
.button{
  margin-top: 20px;
}
</style>
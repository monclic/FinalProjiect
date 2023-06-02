<template>
  <div class="container">
    <v-md-editor v-model="text" height="480px" :toc-nav-position-right="true"></v-md-editor>
  
    <div class="tags">
  <el-tag
    v-for="tag in dynamicTags"
    :key="tag"
    class="mx-1"
    closable
    :disable-transitions="false"
    @close="handleClose(tag)"
  >
    {{ tag }}
  </el-tag>
  <el-input
    v-if="inputVisible"
    ref="InputRef"
    v-model="inputValue"
    class="ml-1 w-20"
    size="small"
    @keyup.enter="handleInputConfirm"
    @blur="handleInputConfirm"
    style="width: 100px;"
  />
  <el-button v-else class="button-new-tag ml-1" size="small" @click="showInput">
    + New Tag
  </el-button>
    </div>

    <div class="buttons">
      <div class="left">
        <div @click="el_dialog_show(1)" class="item-graft">存为草稿</div>
      </div>      
    <div class="right">
      <div @click="el_dialog_show(2)" class="item-cancel">取消</div>   
    <div @click="el_dialog_show(0)" class="item">发布</div>
    </div>
  </div>
  </div>

  <el-dialog v-model="centerDialogVisible" title="" width="30%" center>
      <span> {{ confirm_text }} </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirm_do">
            确认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </template>


<script lang="ts" setup>
import { ref ,nextTick} from 'vue';
import router from '../../router';
import http from '../../utils/http';
import { ElInput } from 'element-plus'
const text=ref('')
const centerDialogVisible = ref(false)
const confirm_text=ref('')
const confirm_ype=ref(-1)

// tags
const inputValue = ref('')
const dynamicTags = ref([])
const inputVisible = ref(false)
const InputRef = ref<InstanceType<typeof ElInput>>()

const handleClose = (tag: string) => {
  dynamicTags.value.splice(dynamicTags.value.indexOf(tag), 1)
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    InputRef.value!.input!.focus()
  })
}

const handleInputConfirm = () => {
  if (inputValue.value) {
    dynamicTags.value.push(inputValue.value)
  }
  inputVisible.value = false
  inputValue.value = ''
}

const el_dialog_show=(type:number)=>{
  if(type==0){
    confirm_text.value='确认发布么'
  }else if(type==1){
    confirm_text.value='确认存为草稿么'
  }else if(type==2){
    confirm_text.value='确认取消编辑么'
  }

  centerDialogVisible.value = true
  confirm_ype.value=type
}
const confirm_do=()=>{
  if(confirm_ype.value==0){
    editor()
  }else if(confirm_ype.value==1){
    editor()
  }else if(confirm_ype.value==2){
    cancel()
  }

  centerDialogVisible.value = false

}

const editor=()=>{
  if(text.value=='')return

  let params={text:text.value,tags:dynamicTags.value.join(" "),type:confirm_ype.value}
  http.post('/longs/LongPublish',params)
}
const cancel=()=>{
  text.value=''
  router.go(-1)
}

</script>
  <style>
.container{
    width: 100%;
    display: flex;
    flex-direction: column;
}
.buttons{
  display: flex;
  flex-direction: row;
  margin-top: 30px;
  justify-content: space-between;
}
.right{
  display: flex;
  flex-direction: row;
}
.item{
  cursor: pointer;
  height: 40px;
  width: 100px;
  background-color: #37c8f7;
  border-radius: 20px;
  color: #fff;
  user-select: none;
  transition: background-color .2s;
}
.item:hover{
  background-color: #57dfff;
}
.item-cancel{
  cursor: pointer;
  height: 40px;
  width: 100px;
  background-color: #999fa0;
  border-radius: 20px;
  margin-right: 60px;
  color: #fff;
  user-select: none;
  text-align: center;
  transition: background-color .2s;
  padding-top:10px ;
}
.item-cancel:hover{
  background-color: #b1b7b8;
}
.item-graft{
  cursor: pointer;
  height: 40px;
  width: 100px;
  background-color: #fff;
  border-radius: 20px;
  margin-right: 40px;
  color: #4ec3dd;
  user-select: none;
  text-align: center;
  transition: background-color .2s;
  padding-top:10px ;
}
.item-graft:hover{
  background-color: #ecf9fe;
}
.tags{
margin-top: 10px;
}
</style>
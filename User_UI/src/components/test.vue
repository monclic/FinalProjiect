<template>
  <el-upload
    v-model:file-list="fileList"
    class="upload-demo"
    action=""
    :http-request="uploadImage"
    multiple
    :on-preview="handlePreview"
    :on-remove="handleRemove"
    :before-remove="beforeRemove"
    :limit="3"
    :on-exceed="handleExceed"
  >
    <el-button type="primary">Click to upload</el-button>
    <template #tip>
      <div class="el-upload__tip">
        jpg/png files with a size less than 500KB.
      </div>
    </template>
  </el-upload>

  <div class="bili-pics-uploader__add" @click="picsUplod"></div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import http from '../utils/http';

const fileList = ref([]);

const handleRemove = (file, uploadFiles) => {
  console.log(file, uploadFiles);
};

const handlePreview = (uploadFile) => {
  console.log(uploadFile);
};

const handleExceed = (files, uploadFiles) => {
  ElMessage.warning(
    `The limit is 3, you selected ${files.length} files this time, add up to ${
      files.length + uploadFiles.length
    } totally`
  );
};

const beforeRemove = (uploadFile, uploadFiles) => {
  return ElMessageBox.confirm(`Cancel the transfer of ${uploadFile.name} ?`).then(
    () => true,
    () => false
  );
};

const uploadImage = async (params) => {
  const img = params.file;
  const formData = new FormData();
  formData.append('file', img);
  try {
    const res = await http.post('/user/Upload', formData);
    console.log(res);
  } catch (error) {
    console.error(error);
  }
};
// 图片
interface Iamge {
    order: number,
    url: String;
}
// 图片列表
const images = ref<Iamge[]>([])
const picsUplod = () => {
    uploadImage1(images.value.length);
}
async function uploadImage1(index: number) {
  try {
    // Open file picker and get file handle
    const [fileHandle] = await window.showOpenFilePicker({
      types: [{
        accept: {
          'image/*': ['.png', '.gif', '.jpeg', '.jpg', '.webp']
        }
      }]
    });

    // Get file data
    const file = await fileHandle.getFile();

    // Create FormData object
    const formData = new FormData();
    formData.append('image', file);

    // Make POST request to API endpoint
    const response = await fetch('/user/Upload', {
      method: 'POST',
      body: formData
    });

    // Handle response
    const data = await response.json();
    console.log(data);
  } catch (error) {
    console.error(error);
  }
}

</script>
<style>
.bili-pics-uploader__add {
    border: 2px dashed #cbcbcb;
    border-radius: 4px;
    cursor: pointer;
    height: 92px;
    margin-bottom: 10px;
    position: relative;
    transition: border-color .3s;
    width: 92px;
    user-select: none;
}
</style>
<template>
    <div class="container">
        <div class="space-left">
            <U_Nav @toggle-div="handleToggleDiv"></U_Nav>
        </div>
        <div class="space-right">
            <div class="space-right-top">
                <div class="title">
                    <span>{{ title }}</span>
                </div>
            </div>
            <div class="space-right-bottom">
                <div class="routerview">
                    <router-view></router-view>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang='ts' setup>
import { ref } from 'vue';
import U_Nav from './user-nav.vue'

const title=ref('')
const handleToggleDiv = (payload:any) => {
  // 处理来自 b.vue 的自定义事件
  // 在此处根据需求修改 div.e 的显示或隐藏逻辑
//   showDiv.value = !showDiv.value;
  // 在这里可以使用 payload 参数传递的值
  title.value=payload
  
}
</script>

<style scoped>
.container {
    height: calc(100vh - 4.6rem);
    width: 1200px;
    display: flex;
    flex-direction: row;
    align-items: stretch;
    margin: 0 auto;
    /* padding-bottom: 20px; */
}

.space-left {
    width: 140px;
    min-width: 140px;
    background-color: rgba(255, 255, 255, 0.8);
}

.space-right {
    flex: 1;
    display: flex;
    flex-direction: column;
    /* flex-grow: 1; */
    background-color: rgba(255, 255, 255, 0.5);
}

.space-right .space-right-top .title {
    height: 42px;
    background-color: #fff;
    box-shadow: 0 2px 4px 0 rgba(121, 146, 185, 0.54);
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    font-size: 15px;
    color: #666;
    border-radius: 4px;
}

.space-right .space-right-top {
    padding: 10px 10px 0;
    z-index: 11;
}

span {
    margin: 0;
    padding: 0;
}

.space-right .space-right-bottom {
    padding: 10px;
    position: relative;
    flex: 1;
    height: calc(100% - 66px);
    overflow:hidden;
}
.routerview{
    overflow-y: auto;
    height: 100%;
    width: 100%;
    padding: 0 20px;
}
</style>
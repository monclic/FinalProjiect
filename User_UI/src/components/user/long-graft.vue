<template>
    <div class="container">
      <div class="masonry_wall">
        <div v-for="(item, index) in state.items">
          <div class="item-wrapper">
            <div class="item-content">
              <div class="content-wrapper">
                <div class="item-info">
                  <div  @click="update(item.id)"  class="item-title">{{ item.username }}</div>
                  <div class="title-util" style="  font-size: 12.432px;">
                    <div  @click="update(item.id)"  class="time">{{ item.createTime }}</div>
                    <div class="delete" @click="delete_(item.id)">
                      删除
                    </div>
  
                  </div>
                  <div @click="update(item.id)" class="item-meta">{{ item.shortContent }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-show="loading" class="loading">Loading...</div>
        <div ref="sentinel" style="height: 40px;"></div>
      </div>
      <el-dialog v-model="centerDialogVisible" title="" width="30%" center>
        <span>
          确认要删除该条short么
        </span>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="centerDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="delete_do">
              确认
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </template>
  <script lang='ts'>
  import { defineComponent } from 'vue';
  
  export default defineComponent({
    name: 'U_Short',
  });
  
  </script>
  <script lang='ts' setup>
  import { ref, onMounted, onUnmounted, reactive, nextTick } from 'vue';
  import { useRouter } from 'vue-router';
  
  import http from '../../utils/http'
  
  
  const short_publish_show = ref(false)
  // 是否有下一页
  const nextPageIs = ref(false)
  function dialogShow() {
    short_publish_show.value = !short_publish_show.value
  }
  // 当前页码
  const currentPage = ref(0);
  // 每页大小
  const PageSize = ref(5)
  
  const router = useRouter();
  const update = (id: String) => {
    router.push('/longs/lupdate/' + id);
  
  }
  
  const loading = ref(false);
  
  const sentinel = ref<HTMLDivElement | null>(null);
  
  const observer = new IntersectionObserver(([entry]) => {
    if (entry.isIntersecting && nextPageIs.value) {
      loadMore();
    }
  });
  
  
  const state = reactive({
    items: [] as any
  });
  const loadMore = async () => {
    if (loading.value) return;
    loading.value = true;
  
    setTimeout(() => {
      nextTick(() => {
        getPage()
        loading.value = false;
      });
    }, 800); //加载动画1s
  };
  
  const getPage = async () => {
    currentPage.value++
    const param = { PageNumber: currentPage.value.toString(), PageSize: PageSize.value}
  
    http.get("/longs/PersonalLongsGraft", param)
      .then((data: any) => {
        state.items = [...state.items, ...data.list]
        nextPageIs.value = data.nextPageIs
  
      })
  };
  

  const setLike = (type: number, contextId: number, index: number) => {
    let param = { type: type, contextId: contextId }
    http.post("/likes/setLike", param)
    state.items[index].likeIs = !state.items[index].likeIs
    if (state.items[index].likeIs) {
      state.items[index].likes = state.items[index].likes + 1
    } else {
      state.items[index].likes = state.items[index].likes - 1
    }
  }
  
  onMounted(() => {
    getPage()
  
    if (sentinel.value) {
      observer.observe(sentinel.value);
    } else { // 延迟执行
      nextTick(() => {
        if (sentinel.value) {
          console.log(111);
          observer.observe(sentinel.value);
        }
      });
    }
  
  });
  
  const centerDialogVisible = ref(false)
  const sid = ref(-1)
  const delete_ = (id: any) => {
    centerDialogVisible.value = true
    sid.value = id
  
  }
  const delete_do = () => {
    
    http.post('/longs/delete', sid.value,{
      headers: {
        'Content-Type': 'text/plain' // 指定请求体的内容类型为纯文本
      }
    })
      .then((data: any) => {
      })
  
    centerDialogVisible.value = false
  }
  onUnmounted(() => {
    observer.disconnect();
  });
  </script>
    
  <style scoped>
  /* .container {
      width: 100%;
    } */
  
  .masonry_wall {
    width: 100%;
  }
  
  .item-wrapper {
    width: 70%;
    border-radius: 4px;
    background-color: #f2f3f4;
    margin-bottom: 1rem;
    margin-left: 70px;
    overflow: hidden;
    margin-top: 10px;
  }
  
  .item-content {
    /* flex-grow放大比例 flex-shrink缩小 flex-basis初始宽度 */
    flex: 1 1 auto;
    position: relative;
    display: flex;
    flex-direction: column;
    margin-top: var(1rem);
    overflow: hidden;
    /* 水平居中 */
    justify-content: center;
    background-color: #fff;
    padding: 0 20px;
  }
  
  
  
  .item-a {
    display: flex;
    flex-shrink: 0;
    width: 100%;
    /* height: 12rem; */
    position: relative;
    border-radius: 5px;
    overflow: hidden;
  }
  
  .imgs {
    flex: 1
  }
  
  img {
    float: left;
    width: auto;
    height: auto;
    max-width: 220px;
    max-height: 220px;
    margin-right: 10px;
    border-radius: 5px;
    margin-top: 4px;
    margin-bottom: 5px;
    margin-right: 10px;
  }
  
  .item-info {
    max-height: 300px;
    flex-grow: 1;
    display: flex;
    flex-direction: column;
    /* justify-content: space-between; */
    padding-right: 0.309rem;
    z-index: 40;
  
  }
  
  .item-title {
    display: flex;
    padding: 0.618rem;
    overflow: hidden;
  }
  
  .item-meta {
    padding: 0 0.618rem;
    line-height: 2em;
    display: flex;
    /* justify-content: space-between; */
    align-items: baseline;
    overflow: hidden;
    font-size: 12.432px;
    white-space: pre-wrap;
    word-wrap: break-word;
    word-break: break-all;
    max-height: 100px;
  }
  
  .shorts-aside {
    order: 4;
    flex-shrink: 0;
    margin-left: 1.236rem;
    height: 300px;
    width: 14.6rem;
    overflow: hidden;
    /* background-color: aqua; */
  }
  
  
  .shorts-aside-wrapper {
    position: fixed;
    display: flex;
    flex-direction: column;
  }
  
  .sort {
    width: 8rem;
  }
  
  .sort_item {
    display: flex;
    align-items: center;
    position: relative;
    width: 100%;
    height: 3em;
    margin-bottom: 0.618rem;
    padding: 0 1.236rem;
    border-radius: 3px;
    overflow: hidden;
    text-decoration: none;
    letter-spacing: .5px;
    user-select: none;
    will-change: background-color;
    transition: background-color .06s;
    background-color: #fff;
  }
  
  .others {
    width: 3.3rem;
    margin-top: 320px;
  }
  
  .others_item {
    border-radius: 6px;
    height: 50px;
    width: 100%;
    text-align: center;
    padding: 4px 4px;
    display: flex;
    position: relative;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    background-color: #fff;
    margin-bottom: 0.618rem;
    user-select: none;
  
  }
  
  
  .pub-wrapper {
    padding: 10px 20px;
    margin: 10px 20px 30px 0;
    background-color: #fff;
    border-radius: 4px;
  }
  
  .loading {
    width: 100%;
    text-align: center;
    margin: 10px;
    font-size: 20px;
    color: gray;
  }
  
  /* 隐藏滚动条 */
  ::-webkit-scrollbar {
    width: 0;
    height: 0;
  }
  
  /* 隐藏滚动条上的滑块 */
  ::-webkit-scrollbar-thumb {
    display: none;
  }
  
  /* 隐藏滚动条的背景 */
  ::-webkit-scrollbar-track {
    display: none;
  }
  
  .utils {
    display: flex;
    flex-direction: row;
    width: 100%;
    justify-content: space-between;
    padding: 10px 60px 0;
  }
  
  .like {
    position: relative;
    cursor: pointer;
    width: 46px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    color: #9499a0;
  }
  
  
  
  .to-reply {
    cursor: pointer;
    width: 46px;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    color: #9499a0;
  }
  
  .side-toolbar__action__text {
    user-select: none;
    margin-top: 4px;
    font-size: 14px;
    line-height: 14px;
    align-items: center;
  }
  
  .content-wrapper {
    display: flex;
    flex-direction: column;
    cursor: pointer;
  }
  
  .title-util {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
  }
  
  .delete {
    color: red;
    cursor: pointer;
  }
  </style>
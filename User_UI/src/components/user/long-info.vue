<template>
    <div class="container">
      <div class="masonry_wall">
        <div v-for="(item, index) in state.items">
          <div class="item-wrapper">
            <div class="item-content">
              <div class="content-wrapper">
                <div class="item-info">
                  <div @click="show_detail(item.id)"  class="item-title">{{ item.username }}</div>
                  <div class="title-util" style="  font-size: 12.432px;">
                    <div class="time">{{ item.createTime }}</div>
                    <div class="delete" @click="delete_(item.id)">
                      删除
                    </div>
  
                  </div>
                  <div @click="show_detail(item.id)" class="item-meta">{{ item.shortContent }}</div>
                </div>
              </div>
              <div class="utils">
                <!-- 点赞 -->
                <div @click="setLike(2, item.id, index)" class="like">
                  <div v-show="item.likeIs" class="is_like">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path fill-rule="evenodd" clip-rule="evenodd"
                        d="M20.3186 7.74998H15.776C15.999 7.26616 16.2333 6.6567 16.3213 6.06668C16.3848 5.64061 16.3909 5.11874 16.3397 4.62199C16.2897 4.1373 16.1773 3.59536 15.9528 3.16982C15.4775 2.26864 14.8233 1.63578 14.0361 1.48224C13.1988 1.31893 12.4779 1.74495 12.0464 2.39778C11.6856 2.94375 11.5449 3.39925 11.4167 3.81444L11.4167 3.81444L11.4125 3.82786C11.2892 4.22696 11.1679 4.61901 10.8559 5.17154C10.3427 6.08056 9.92397 6.55203 9.36125 7.0454C9.071 7.29988 8.77253 7.47246 8.5 7.5831V21.225C9.28742 21.2418 10.1289 21.2524 11 21.2524C13.3441 21.2524 15.3224 21.1759 16.627 21.1071C17.9072 21.0396 19.1311 20.3943 19.7997 19.2217C20.4316 18.1135 21.2409 16.4358 21.7288 14.4296C22.1856 12.5508 22.3761 11.0232 22.4535 9.98474C22.5487 8.70656 21.5102 7.74998 20.3186 7.74998ZM7 21.1842V7.74998H5.30231C3.75929 7.74998 2.38771 8.85084 2.21213 10.4483C2.10046 11.4642 2 12.7852 2 14.2524C2 15.839 2.11749 17.3111 2.23926 18.4118C2.40726 19.9306 3.65916 21.0399 5.15251 21.1103C5.68498 21.1355 6.30853 21.1615 7 21.1842Z"
                        fill="#00A1D6"></path>
                    </svg>
                  </div>
                  <div v-show="!item.likeIs" class="not_like">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                      <path fill-rule="evenodd" clip-rule="evenodd"
                        d="M13.9252 3.04546C13.6432 3.01258 13.382 3.13521 13.2422 3.29868C12.9774 3.60848 12.8705 3.86276 12.7384 4.23799C12.7234 4.28037 12.7082 4.32422 12.6925 4.36961C12.5611 4.74857 12.3929 5.23391 12.044 5.85187C11.446 6.91117 10.8882 7.55173 10.2013 8.154C9.63865 8.6473 9.06639 8.98294 8.5 9.14275V19.7248C9.28689 19.7417 10.1287 19.7525 11 19.7525C13.2468 19.7525 15.1529 19.6812 16.4372 19.615C17.3477 19.5681 18.1379 19.1176 18.5497 18.3851C19.1274 17.3574 19.8396 15.8503 20.2712 14.0753C20.6734 12.4212 20.8569 11.0615 20.9392 10.1025C20.9774 9.65797 20.6256 9.25003 20.08 9.25003H15.0977C14.8476 9.25003 14.614 9.12542 14.4748 8.91775C14.3355 8.71015 14.3089 8.44684 14.4037 8.21557C14.4037 8.21555 14.4037 8.21553 14.4037 8.21551C14.4037 8.21547 14.4038 8.21543 14.4038 8.21539C14.4038 8.21537 14.4038 8.21536 14.4038 8.21534L14.4046 8.21341L14.4085 8.20377L14.4249 8.16256C14.4395 8.12573 14.4609 8.07081 14.4874 8.00089C14.5404 7.86084 14.6131 7.66185 14.6909 7.42856C14.8489 6.95439 15.0177 6.36917 15.0941 5.85681C15.2109 5.07451 15.1824 4.44592 14.8757 3.86439C14.5461 3.23954 14.1724 3.07429 13.9252 3.04546ZM16.164 7.75003H20.08C21.4037 7.75003 22.5555 8.81291 22.4337 10.2309C22.3455 11.2583 22.1508 12.6941 21.7288 14.4297C21.2555 16.3758 20.4798 18.0127 19.8573 19.1201C19.1594 20.3616 17.8654 21.0435 16.5144 21.113C15.21 21.1802 13.2777 21.2525 11 21.2525C8.7933 21.2525 6.77664 21.1846 5.34776 21.1195C3.73985 21.0461 2.39101 19.8517 2.21798 18.2152C2.1042 17.1391 2 15.7467 2 14.2525C2 12.8835 2.08746 11.6418 2.18985 10.6567C2.36874 8.93544 3.84615 7.75003 5.50754 7.75003H7.75C8.06896 7.75003 8.56382 7.59478 9.21241 7.02612C9.77509 6.53279 10.2246 6.0235 10.7378 5.11442C11.0177 4.61868 11.146 4.24973 11.2753 3.87788C11.2913 3.83198 11.3072 3.78604 11.3235 3.73981C11.4791 3.29802 11.6602 2.84083 12.1021 2.32398C12.5582 1.79047 13.3077 1.4633 14.0989 1.55555C14.9248 1.65186 15.6866 2.18668 16.2024 3.16456C16.7233 4.15199 16.7157 5.15349 16.5777 6.07814C16.4916 6.65522 16.3202 7.26593 16.164 7.75003ZM7 19.6835V9.25003H5.50754C4.54442 9.25003 3.77423 9.92257 3.68182 10.8118C3.58359 11.7569 3.5 12.9457 3.5 14.2525C3.5 15.6802 3.59975 17.0179 3.70967 18.0575C3.80099 18.9212 4.51252 19.5798 5.41611 19.621C5.88508 19.6424 6.41785 19.6641 7 19.6835Z"
                        fill="currentColor"></path>
                    </svg>
                  </div>
                  <div class="side-toolbar__action__text">{{ item.likes }}</div>
                </div>
                <div @click="show_detail(item.id)" class="to-reply">
                  <!-- 回复数 -->
                  <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path fill-rule="evenodd" clip-rule="evenodd"
                      d="M2 11C2 6.58172 5.58172 3 10 3H14C18.4183 3 22 6.58172 22 11C22 15.2656 18.6616 18.7514 14.4549 18.9873C13.4971 20.2148 11.9228 21.7529 9.71136 22.3555C9.2837 22.472 8.89202 22.2894 8.673 22.0075C8.46064 21.7342 8.38542 21.3424 8.5439 20.9775C8.87531 20.2145 8.94851 19.5143 8.90558 18.9257C5.00479 18.3918 2 15.0471 2 11ZM10 4.5C6.41015 4.5 3.5 7.41015 3.5 11C3.5 14.459 6.20234 17.2875 9.6105 17.4885C9.95008 17.5086 10.2337 17.7547 10.3013 18.088C10.4401 18.7716 10.4842 19.6217 10.2587 20.5643C11.6947 19.9026 12.7693 18.7583 13.4679 17.806C13.6076 17.6155 13.829 17.502 14.0652 17.4997C17.6249 17.4648 20.5 14.5681 20.5 11C20.5 7.41015 17.5899 4.5 14 4.5H10Z"
                      fill="currentColor"></path>
                  </svg>
                  <div class="side-toolbar__action__text">{{ item.replys }}</div>
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
  const show_detail = (id: String) => {
    router.push('/longs/ldetail/' + id);
  
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
  
    http.get("/longs/PersonalLongs", param)
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
  
  defineExpose({ show_detail })
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
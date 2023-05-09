<template>
  <div class="container">
    <div class="shorts-main">
      <div class="pub-wrapper" v-show="short_publish_show">
        <short_publish></short_publish>
      </div>
      <div class="masonry_wall">
        <MasonryWall :items="state.items" :column-width="350" :gap="1">
          <template #default="{ item, index }">
            <div @click="show_detail" class="item-wrapper">
              <div class="item-content">
                <div class="item-info">
                  <div class="item-title">{{ item.username }}</div>
                  <div class="item-meta">{{ item.shortContent }}</div>
                </div>
                <div class="item-a">
                  <!-- <img :src="'../public/' + item.imagesList[0]"> -->
                </div>
              </div>
            </div>
          </template>
        </MasonryWall>
        <div v-show="loading" class="loading">Loading...</div>
        <div ref="sentinel" style="height: 40px;"></div>
      </div>
    </div>
    <div class="shorts-aside">
      <!-- <span style="color:beige"> tag rank publish</span> -->
      <div class="shorts-aside-wrapper">
        <div class="sort">
          <div class="sort_item">综合排序</div>
          <div class="sort_item">最新发布</div>
          <div class="sort_item">最多点赞</div>
          <div class="sort_item">最多回复</div>
        </div>
        <div class="others">
          <div class="others_item" @click="dialogShow">
            <span>点击发布</span>

          </div>
          <div @click="" class="others_item">返回顶部</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang='ts' setup>
import { ref, onMounted, onUnmounted, reactive, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import short_publish from './short-publish.vue'
import http from '../../utils/http';


const short_publish_show = ref(false)
// 是否存在图片
const imagesIs = ref(false)
// 是否有下一页
const nextPageIs = ref(false)
function dialogShow() {
  short_publish_show.value = !short_publish_show.value
}
// 当前页码
const currentPage = ref(0);
// 每页大小
const PageSize=ref(5)
interface short {
  id: number,
  userId: number,
  shortContent: string,
  articleStatus: number,
  createTime: string,
  images: string,
  likes: number,
  replys: number

}


const router = useRouter();
const show_detail = () => {
  router.push({path:'/shorts/sdetail',query:{id:'121'}});

}

const loading = ref(false);

const sentinel = ref<HTMLDivElement | null>(null);

const observer = new IntersectionObserver(([entry]) => {
  if (entry.isIntersecting && nextPageIs.value) {
    loadMore();
  }
});


const state = reactive({
  items: [] as short[]
});
const loadMore = async () => {
  console.log(111);

  if (loading.value) return;
  loading.value = true;

  setTimeout(() => {
    nextTick(() => {
      getPage()
      loading.value = false;
    });
  }, 1000); //加载动画1s
};

const getPage = async () => {
  currentPage.value++
  const param = { PageNumber: currentPage.value.toString(), PageSize: PageSize.value }

  http.get("/shorts/ShortList", param)
    .then((data: any) => {
      state.items = [...state.items, ...data.list]
      nextPageIs.value = data.nextPageIs

    })
};



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


onUnmounted(() => {
  observer.disconnect();
});
</script>

<style scoped>
.container {
  display: flex;
  flex-grow: 1;
  width: 100%;
}

.masonry_wall {
  width: 100%;
}

.item-wrapper {
  width: 95%;
  border-radius: 4px;
  background-color: #f2f3f4;
  margin-bottom: 1rem;
  padding: 10px 30px 10px;
  overflow: hidden;
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
  cursor: pointer;
}



.item-a {
  height: 150px;
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
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-right: 0.309rem;
  z-index: 40;

}

.item-title {
  display: flex;
  padding: 0.618rem;
  overflow: hidden;
}

.item-meta {
  height: 2em;
  padding: 0 0.618rem;
  line-height: 2em;
  display: flex;
  /* justify-content: space-between; */
  /* 容器中的项目将以其文本基线（text baseline）对齐，而不是默认的盒模型基线（box baseline）。文本基线是所有字形的底部对齐线，而盒模型基线是元素边框的底部线。 */
  align-items: baseline;
  overflow: hidden;
  font-size: 12.432px;
  /* 强制文本在一行内显示，不允许换行。 */
  white-space: nowrap;
  /* 如果文本超出了容器的宽度，使用省略号 ... 表示被截断的文本部分。 */
  text-overflow: ellipsis;
  /* 如果一个单词太长无法放在一行内，就将整个单词移到下一行，不允许将单词拆分成两行。 */
  word-wrap: normal;
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
  margin-top: 260px;
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

.shorts-main {
  order: 3;
  position: relative;
  flex-grow: 1;
  /* display: flex; 使用masonry-wall不能在这里用这个*/
  overflow: hidden;
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
}</style>
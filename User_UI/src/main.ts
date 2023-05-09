import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import MasonryWall from '@yeger/vue-masonry-wall'
import http from './utils/http'

const app = createApp(App)


app.use(ElementPlus)
    .use(router)
    .use(MasonryWall)
app.mount('#app')
import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import MasonryWall from '@yeger/vue-masonry-wall'
import http from './utils/http'
//Markdown工具
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
// 引入github 主题
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
// 代码行号
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
// 代码复制
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';

// highlightjs
import hljs from 'highlight.js';

const app = createApp(App)

// markdown编辑器
VueMarkdownEditor
.use(githubTheme, {
  Hljs: hljs,
})
.use(createLineNumbertPlugin())
.use(createCopyCodePlugin());

app.use(ElementPlus)
    .use(router)
    .use(MasonryWall)
    .use(VueMarkdownEditor)
app.mount('#app')
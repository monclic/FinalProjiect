import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 8089, // 设置端口号为8088
    proxy: {
      "/api": { // “/api” 以及前置字符串会被替换为真正域名
        target: "http://127.0.0.1/", // 请求域名
        secure: false, // 请求是否为https
        changeOrigin: true, // 是否跨域
        rewrite: (path) => path.replace(/^\/api/, "")
      }
    }
  }
})

// 跨域 https://blog.51cto.com/u_14316538/6030423

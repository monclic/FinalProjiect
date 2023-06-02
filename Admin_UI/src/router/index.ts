//引入路由对象
import path from 'path'
import { createRouter, createWebHistory, createWebHashHistory, createMemoryHistory, RouteRecordRaw } from 'vue-router'

//路由数组的类型 RouteRecordRaw
// 定义一些路由
// 每个路由都需要映射到一个组件。
const routes: Array<RouteRecordRaw> = [{
    path:'/',
    component:() => import('../components/first.vue'),        
    children:[
     {
        path:'',
        component:()=>import('../components/user/user-info.vue'),
        children:[
            {
                path: 'short',
        component: () => import('../views/short-info.vue'),        
            },{
                path: 'long',
        component: () => import('../views/long-info.vue'),        
            },{
                path: 'board',
        component: () => import('../views/board.vue'),        
            },{
                path: 'admins',
        component: () => import('../views/admins.vue'),        
            }
        ]
     }
    ]
},,{
    path: '/login',
component: () => import('../views/login.vue'),        
}
]



const router = createRouter({
    history: createWebHistory(),
    routes
})

//导出router
export default router
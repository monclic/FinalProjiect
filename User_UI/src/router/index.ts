//引入路由对象
import { createRouter, createWebHistory, createWebHashHistory, createMemoryHistory, RouteRecordRaw } from 'vue-router'

//路由数组的类型 RouteRecordRaw
// 定义一些路由
// 每个路由都需要映射到一个组件。
const routes: Array<RouteRecordRaw> = [{
    path: '/',
    component: () => import('../components/first.vue')
},{
    path: '/test',
    component: () => import('../components/test.vue'),
    children: [
        {
            path: 'testVV',
            component: () => import('../components/testVV.vue')
        },{
            path: 'VV',
            component: () => import('../components/testVV.vue')
        }        
      ]
},{
    path: '/section',
    component: () => import('../components/section.vue')
},{
    path: '/testVV',
    component: () => import('../components/testVV.vue')
},{
    path: '/shorts',
    component: () => import('../components/shorts.vue')
}
]



const router = createRouter({
    history: createWebHistory(),
    routes
})

//导出router
export default router
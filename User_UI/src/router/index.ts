//引入路由对象
import path from 'path'
import { createRouter, createWebHistory, createWebHashHistory, createMemoryHistory, RouteRecordRaw } from 'vue-router'

//路由数组的类型 RouteRecordRaw
// 定义一些路由
// 每个路由都需要映射到一个组件。
const routes: Array<RouteRecordRaw> = [{
    path: '/',
    component: () => import('../components/first.vue'),
    children: [
        {
            path: 'shorts',
            component: () => import('../components/Short/shorts.vue'),
            children: [
                {
                    path: '',
                    component: () => import('../components/Short/shorts-masonry-wall.vue')
                }, {
                    path: 'sdetail/:sdid',
                    component: () => import('../components/Short/short-detailed.vue')
                }, 
            ]
        },{
            path: 'info',
            component: () => import('../components/user/user-info.vue'),
            children:[
                {
                    path: 'short',
            component: () => import('../components/user/short-info.vue'),        
                },{
                    path: 'long',
            component: () => import('../components/user/long-info.vue'),        
                },{
                    path: 'space',
            component: () => import('../components/user/user_info_change.vue'),        
                },{
                    path: 'lgraft',
            component: () => import('../components/user/long-graft.vue'),        
                }
            ]
        },{
            path:'longs',
            component:()=>import('../components/Long/long.vue'),
            children:[
                {
                    path: '',
                    component: () => import('../components/Long/longs-list.vue')
                },{
                    path:'lpub',
                    component:()=>import('../components/Long/long-publish.vue')
                },{
                    path:'ldetail/:lid',
                    component:()=>import('../components/Long/long-detail.vue')
                },{
                    path:'lupdate/:lid',
                    component:()=>import('../components/Long/long-update.vue')
                }
            ]
        },{
            path:'board',
            component:()=>import('../components/board.vue')
        }
    ]
}, {
    path: '/login',
    component: () => import('../components/login/login.vue'),
    // children:[

    // ]
},
{
    path: '/retrievepwd',
    component: () => import('../components/login/find-pwd.vue')
},

{
    path: '/test',
    component: () => import('../components/test.vue'),
    children: [
        {
            path: 'testVV',
            component: () => import('../components/testVV.vue')
        }, {
            path: 'VV',
            component: () => import('../components/testVV.vue')
        }
    ]
}, {
    path: '/section',
    component: () => import('../components/section.vue')
}, {
    path: '/testVV',
    component: () => import('../components/testVV.vue')
}, {
    path: '/m',
    component: () => import('../components/reply/reply_publish.vue')
}
]



const router = createRouter({
    history: createWebHistory(),
    routes
})

//导出router
export default router
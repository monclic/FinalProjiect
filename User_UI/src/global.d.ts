// 解决报错：TypeScript 中的 Window 类型不存在属性
// https://bobbyhadz.com/blog/typescript-property-does-not-exist-on-type-window
// src/global.d.ts
export {}
 
declare global {
    interface Window {
        showOpenFilePicker: any;//全局变量名
    }
}
<template>
    <!-- <el-dialog class="dialog" v-model="dialogTableVisible" style="  z-index: 999;" title=""> -->
    <!-- <div  v-show="dialogTableVisible"></div> -->
    <!-- 编辑区 -->
    <div class="bili-dyn-publishing__input">
        <div class="bili-rich-textarea" style="max-height: 180px;">

            <div contenteditable="true" class="bili-rich-textarea__inner" @paste="handlePaste" ref="editor"
                @mouseup="saveSelection" @keyup="saveSelection" @click="saveSelection"
                style="font-size: 15px; line-height: 24px; min-height: 24px;"></div>
        </div>

    </div>
    <!-- 图片上传 -->
    <div class="bili-dyn-publishing__image-upload" v-show="images_show" style="">
        <div class="bili-pics-uploader">
            <div class="bili-pics-uploader__content">
                <!-- 缩略图 -->
                <div class="bili-pics-uploader__item success" v-for="(item, index) in images" :key="index" style="">
                    <div class="bili-pics-uploader__item__remove" @click="removeById(item.order)"></div>
                    <div class="bili-pics-uploader-item-preview" status="SUCCESS" msg="">
                        <div class="bili-pics-uploader-item-preview__pic"
                            :style="{ backgroundImage: 'url(' + item.url + ')' }">
                        </div>
                    </div>
                </div>
                <!-- 图片上传窗口 -->
                <div class="bili-pics-uploader__add" @click="picsUplod"></div>
            </div>
        </div>
    </div>
    <!-- 工具组件 -->
    <div class="meta">
        <div class="tools">
            <!-- emoji -->
            <div :class="{ 'bili-dyn-publishing__tools__item': true, 'emoji': true, 'active': isEmojiActive }"
                @click="emojisClick" style="position: relative;">
                <div role="tooltip" class="bili-popover bili-popover-shape" @click.stop v-show="isEmojiActive">
                    <div class="bili-emoji">
                        <div class="bili-emoji__content">
                            <ul class="bili-emoji__list" style="overflow: auto;">
                                <li v-for="(emoji, index) in emojis" @click="insertImage(emoji.id)" :key="index"
                                    class="bili-emoji__list__item bili-emoji__list__item small">
                                    <img :src="emoji.id" alt="">
                                </li>
                                <li></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 图片上传 -->
            <div :class="{ 'tools-item': true, 'upload': true, 'active': isUploadActive }" @click="handleClick"></div>
        </div>
        <div class="publishing__headquarters">
            <button @click="publish" style="width: 70px; height: 30px; background-color: #00aeec; color:#fff
            ;user-select: none;">发布</button>
        </div>
    </div>
    <!-- </el-dialog> -->
</template>
<script lang="ts">
import { defineComponent } from 'vue';
import http from '../../utils/http';
import { url } from 'inspector';
export default defineComponent({
    name: 'short_publish',

});
</script>
<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessageBox } from 'element-plus'

// 使用ref可以让组件做到实时响应
const dialogTableVisible = ref(false)

// 控制缩略图组件
const images_show = ref(false)
// emoji
const isEmojiActive = ref(false);
// 控制工具栏图片上传按键
const isUploadActive = ref(false);
// 图片
interface Iamge {
    order: number,
    url: String;
}
// 缩略图列表
const images = ref<Iamge[]>([])
// 图片列表
const images_list = ref<Iamge[]>([])
function dialogShow() {
    dialogTableVisible.value = true
}

// 处理粘贴事件
const handlePaste = (e: any) => {
    e.preventDefault();

    const items = e.clipboardData.items;

    for (let i = 0; i < items.length; i++) {
        const item = items[i];

        if (item.type.indexOf('image') !== -1) {
            const blob = item.getAsFile();
            const reader = new FileReader();
            reader.onload = () => {
                // 将图片转换为 dataURL 格式，并插入到可编辑元素中
                const img = `<img src="${reader.result}" />`;
                document.execCommand('insertHTML', false, img);
            }
            reader.readAsDataURL(blob);
        } else if (item.type.indexOf('text/plain') !== -1) {
            // 如果是文本则直接插入
            item.getAsString((text: string) => {
                document.execCommand('insertText', false, text);
            });
        }
    }
}

// 图片上传 插入编辑区
function handleClick() {
    if (!isUploadActive.value) {
        images_show.value = !images_show.value;
        isUploadActive.value = !isUploadActive.value;
        uploadImage(images.value.length);
    }
    else {
        if (images.value.length != 0) {
            // 确认弹窗
            ElMessageBox.confirm(
                '关闭将清除所有已添加图片',
                '图片上传',
                {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: 'warning',
                    center: true,
                }
            )
                .then(() => {
                    // 确认
                    images_show.value = !images_show.value;
                    isUploadActive.value = !isUploadActive.value;
                    images.value = []
                })
                .catch(() => {
                    // 取消
                })
        } else {
            images_show.value = !images_show.value;
            isUploadActive.value = !isUploadActive.value;
        }
    }
}
// 删除指定缩略图
const removeById = (id: number) => {
    if (id !== -1) {
        images.value.splice(id - 1, 1);
        images_list.value.splice(id - 1, 1);
    }
}
// 点击图片上传框进行上传
const picsUplod = () => {
    uploadImage(images.value.length);
}
// 图片上传
function uploadImage(index: number) {
    // 打开文件
    window.showOpenFilePicker({
        types: [{
            accept: {
                'image/*': ['.png', '.gif', '.jpeg', '.jpg', '.webp']
            }
        }],
        // 可以选择多个图片
        multiple: true
    }).then(async (arrFileHandle: any) => {
        // 遍历选择的文件
        for (const fileHandle of arrFileHandle) {
            // 获取文件内容
            const fileData = await fileHandle.getFile()

            // 创建一个 Image 对象
            const image = new Image();

            // 设置 Image 对象的 src 为选择的文件的 URL，这样它就会加载图片数据
            image.src = URL.createObjectURL(fileData);

            // 等待图片加载完成
            image.onload = function () {
                // 创建一个 Canvas 对象
                const canvas = document.createElement('canvas');
                canvas.width = image.width;
                canvas.height = image.height;

                // 在 Canvas 上绘制图片
                const context = canvas.getContext('2d');
                if (context) {
                    context.drawImage(image, 0, 0);
                }

                // 获取图片的 base64 数据 URI
                const dataURI = canvas.toDataURL('image/jpeg');
                images.value.push({ order: index + 1, url: dataURI })

                const formData = new FormData();
                formData.append('file', fileData); // 假设 fileData 是上传的文件数据

                http.post('/user/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data' // 指定请求体的内容类型为 multipart/form-data
                    }
                }).then((data:any)=>{                    
                    images_list.value.push({ order: index + 1, url: data.goods })
                })
                
            }
        }
    })
}
const emojis = ref([
    { id: 'http://127.0.0.1/imgs/i_f01.png', alt: 1 },
    { id: 'http://127.0.0.1/imgs/i_f02.png', alt: 2 },
    { id: 'http://127.0.0.1/imgs/i_f03.png', alt: 3 },
    { id: 'http://127.0.0.1/imgs/i_f04.png', alt: 4 },
    { id: 'http://127.0.0.1/imgs/i_f05.png', alt: 5 },
    { id: 'http://127.0.0.1/imgs/i_f06.png', alt: 6 },
    { id: 'http://127.0.0.1/imgs/i_f07.png', alt: 7 },
    { id: 'http://127.0.0.1/imgs/i_f08.png', alt: 8 }])

const emojisClick = () => {
    isEmojiActive.value = !isEmojiActive.value;
}

const editor = ref<HTMLDivElement | null>(null);
let savedRange: Range | null = null;

function saveSelection() {
    const selection = window.getSelection();
    if (selection?.rangeCount) {
        savedRange = selection.getRangeAt(0).cloneRange();
    }
    // console.log(savedRange?.startOffset);
    //不能保存当前的 window.getSelection()，因为点击空白处时会被重置
}

function insertImage(emoji:string) {
    const selection = window.getSelection();
    if (!selection) return;
    if (selection.rangeCount == 0
        || selection?.getRangeAt(0).commonAncestorContainer.parentElement?.classList.length == 0
        || !selection?.focusNode?.parentElement?.classList.contains('bili-rich-textarea__inner')) {
        noFcous(selection)
    } else {
        isFcous(selection)
    }
    // 输入框已获得焦点
    function isFcous(selection: Selection) {
        const parentElement = selection?.focusNode?.parentElement;

        const inner = !parentElement?.classList.contains('bili-rich-textarea__inner')
        if (inner) {
            return;
        }
        // 获取当前光标位置
        let range = savedRange;
        if (!range) range = selection?.getRangeAt(0);
        const insertNode = document.createElement('img');
        insertNode.src = emoji;

        // 插入图片
        range.insertNode(insertNode);

        // 设置图片后面的范围
        const newRange = document.createRange();
        newRange.setStartAfter(insertNode);
        newRange.setEndAfter(insertNode);

        // 移动光标到范围末尾,即插入图片后使光标移动至插入的图片的后面 
        if (selection) {
            selection.removeAllRanges();
            selection.addRange(newRange);
            savedRange = selection.getRangeAt(0).cloneRange();


        }

    }
    // 输入框未获得焦点
    function noFcous(selection: Selection) {
        const insertNode = document.createElement('img');
        insertNode.src = emoji;
        // console.log(savedSelection);  
        const divA = editor.value;
        if (selection.rangeCount == 0) {
            if (!divA) return;
            // 让 div.a 获得焦点      
            if (!savedRange) { divA.focus(); }
        }

        let range: Range | null = null;
        if (savedRange) {
            range = savedRange;
        } else {
            range = selection.getRangeAt(0);
        }
        // console.log(savedSelection);
        //  console.log(range);
        // 插入图片
        range.insertNode(insertNode);

        // 把光标移动到插入的图片后面
        const nextNode = insertNode.nextSibling;
        const newRange = document.createRange();

        // 如果插入的图片是最后一个子节点，那么 nextSibling 属性为 null。
        // 这种情况下可以将新的 Range 对象的起点设置为 insertNode 本身，这样就可以将光标设置在图片后面了
        if (!nextNode) {
            newRange.setStartAfter(insertNode);
            newRange.setEndAfter(insertNode);

        } else {
            newRange.setStartAfter(nextNode)
            newRange.setEndAfter(nextNode);
        }

        // newRange.collapse(true);
        selection.removeAllRanges();
        selection.addRange(newRange);
        savedRange = selection.getRangeAt(0).cloneRange();


    }

}

// 发布
const publish = async () => {
    try {
        const content = editor.value?.innerHTML
        // editor.value!.innerHTML=content+"22"
        const imageUrls = images_list.value
            .sort((a, b) => a.order - b.order) // 按 order 升序排序
            .map(image => image.url) // 取出每个对象的 url 属性
            .join(",")

        if (content == "" && imageUrls == "") return
        const param = { context: content, images: imageUrls }
        await http.post("/shorts/ShortPublish", param)
            .then(
                // 报错无影响
                editor.value!.innerHTML = ""

            )
        // 在这里对返回值进行处理
    } catch (error) {
        console.error(error);
    }
}

// setup中必须使用defineExpose
defineExpose({ dialogShow })
</script>
  
  
<style>
/* 去除dialog自带的header 不能在scope内 */
.el-dialog__header {
    padding: 0 !important;
    padding-bottom: 0 !important;
}

.el-dialog__headerbtn {
    height: 0;
    width: 0;
}

.el-dialog__headerbtn>.el-icon {
    display: none !important;
}

.el-dialog__body {
    padding: 10px !important;
    border: 11px solid rgba(8, 164, 221, 0.3);
}
</style>
  
<style scoped>
.dialog {
    border-radius: 10px;
    border: 12px solid slategray;
}

.bili-dyn-publishing__input {
    margin: 4px 14px 12px 0;
    min-height: 70px;
    padding-right: 4px;
    position: relative;
}

.bili-rich-textarea {
    cursor: text;
    height: 100%;
    overflow-y: auto;
    position: relative;
    width: 100%;
}

.bili-rich-textarea__inner {
    /* 当一个单词过长无法完整放入一行时，将该单词拆分为多行显示。 */
    word-wrap: break-word;
    box-sizing: border-box;
    font-size: 14px;
    letter-spacing: 1px;
    outline: none;
    padding-left: 1rem;
    padding-right: 5px;
    position: relative;
    vertical-align: baseline;
    /* 保留空格和换行符，自动换行。 */
    white-space: pre-wrap;
    width: 100%;
    /* 任意位置断开单词。 */
    word-break: break-all;
}

.bili-rich-textarea__inner:empty::before {
    content: '有什么想和大家分享的？';
    color: grey;

}

.bili-dyn-publishing__image-upload {
    margin-top: 2px;
}

.bili-pics-uploader {
    width: 100%;
}

.bili-pics-uploader div {
    box-sizing: border-box;
}

.bili-pics-uploader__content {
    display: flex;
    flex-wrap: wrap;
}

.bili-pics-uploader div {
    box-sizing: border-box;
}

.bili-pics-uploader-item-preview {
    height: 100%;
    position: relative;
    width: 100%;
}

.bili-pics-uploader-item-preview__pic {
    background-position: 50%;
    background-size: cover;
    border-radius: 4px;
    height: 100%;
    width: 100%;
}



.bili-pics-uploader__item {
    background-color: #f0f0f0;
    border-radius: 4px;
    /* cursor: move; */
    height: 92px;
    margin-bottom: 8px;
    margin-right: 7px;
    position: relative;
    width: 92px;
}

.bili-pics-uploader__item__remove {
    background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAQvSURBVHgB7ZlNbBNXEIBn3qZpiZNDU8mOwVQiB9oqtUXoJeFKz4lUqZUaVYK2VOqhPVaq6BlUqccUCcQBgVBAgJDInVMQEBAJeHMJB+disLMSF4gDBPyGN5sYjPfvPXtxgvB38frtrHbm7byZefMAOnT4sEGIiWx2NGt9LAdB4tdENAiICTWc2LhdAaJlRHSqEpegCrZt37AhBloyIJfLJSzrkzGJYhzeKKsJOerl9ss1OLewMLsMTdKUAay4ED0/koBxiAEEutqsIcYGDA+PqBmHCTCe8SjIESSn5udvXzV5yjIR3rNn5JCadVa+G2IHE4RiJJ3KJMrl4pz2UzpCrstY2/4hxCy0A6ICVZ8ezufzlShRARqgte1o25R3X4iDPGE6opEuxG4DAkeh3SCmdNwp1IDc8Oh+9Y0OwmaB8GVqIFNZLhcXg0QCXSiX25dEkBOwyaCKeLwGg+4HGiC6aEI9nYTNx805QTd9DeDZJ6D9sEXghBn0FXwNcGffgD//+H3XwQM/ZXTlWdZEniGR8M36Xb7CJLPsfLrK/PrzgSG+3p5O9xz997/7YfKH//5r9w/ff7ebr/v6ej+a/P/4EmggBI2pnynPeOMAV5Umvl9vJivGCgbJ1ivPrDxZeQH6JLLfjHpykSeMprbv/FZNvnbSunsv/7i/vx+Ghr76jP/zL/+fuXb9EYQof+Hi5fuTx/RmvwZJWnLKDxZDDUinM+NqWo38k5UNM8JP+ShX8wVxbblUnKkf8q4BAUkgMKamUE3ReoVjUR7csnuwccxrAEHTsT/MCKYV5ddBTyj1C6Mt1fmsICvaON668i5aBrxX+BkQWYOH0bhga0SFWE08unkNIGraAL9oU+9OLRtB4Nkz+2Ri5NhsvJCjQmXjwm5mPRCC0zjmyQPJgc8/RaS9YECU8lF5QtsAiTNOuWiHGpDakelW5YF2JerWQr+s10J+yocZ8ez581XO5KAJCTznlIpvfQXPGrDvuB0z7XXQ15vojlK+RmOINevpkLOh21v4VqNS4rSq/gI3EfVMHjtRWFmprPH1qdNni1HybESpVFrtVZWojnwN7uIFjHvhzQN29ZyHLUR1jQ75de58Exn3YyTANGwRuPUY1HYMzMT4cpU3Dy0ltXggh/umQXcDDXC/gqDAB9sF90vDmr6hfSHn4YPFZDrTqxbKF7AJsBvfm791KUwmsjOn4u5cemAHbzNT0E5UfzQ/P3skSkyrGpXVp0dUHVKANkGENjd3dWSNckl278hvyuIxeIew29hzN0/qyhudD7A7pQZ2Ou7WDjHmAw7goHHGvjM7ZfJQk0dM+5Kgml8YU/eOZ53Dts55QCMtHvJtGLLeCDMtwSsScRpfVK40o3iN+I5ZuelUhawlcJckmVS9JY5ar49ZpdooWWgVQFQXqiQKfoVZhw4dzHkFUC3Woi8BiWsAAAAASUVORK5CYII=) 50%/cover no-repeat;
    cursor: pointer;
    height: 24px;
    position: absolute;
    right: 2px;
    top: 2px;
    width: 24px;
    z-index: 2;
}

.bili-pics-uploader__add {
    border: 2px dashed #cbcbcb;
    border-radius: 4px;
    cursor: pointer;
    height: 92px;
    margin-bottom: 10px;
    position: relative;
    transition: border-color .3s;
    width: 92px;
    user-select: none;
}

.bili-pics-uploader__add:after,
.bili-pics-uploader__add:before {
    background-color: #bdc0c5;
    border-radius: 4px;
    content: "";
    height: 4px;
    left: 50%;
    position: absolute;
    top: 50%;
    transition: background-color .3s;
    width: 28px;
}

.bili-pics-uploader__add:before {
    transform: translate3d(-50%, -50%, 0);
}

.bili-pics-uploader__add:after {
    transform: translate3d(-50%, -50%, 0) rotate(90deg);
}

.bili-pics-uploader__add:hover {
    border-color: #80daf6;
}

.bili-pics-uploader__add:hover:after,
.bili-pics-uploader__add:hover:before {
    background-color: #80daf6;
}

.meta {
    align-items: center;
    display: flex;
    justify-content: space-between;
}

.tools {
    align-items: center;
    display: flex;
    position: relative;
}

.publishing__headquarters {
    align-items: center;
    display: flex;
    justify-content: center;
    margin-left: 20px;
}

.tools-item {
    background-repeat: no-repeat;
    background-size: contain;
    cursor: pointer;
    height: 24px;
    margin-left: 16px;
    /* 禁止用户选择文本内容。 这个属性可以防止用户复制网页上的文本内容，或者在网页中使用鼠标拖动选中文本。 */
    user-select: none;
    width: 24px;
}

.tools-item.upload {
    background-image: url(data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBjbGlwLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik0xMiA0Ljk5OGMtMi41MTggMC00LjcxNy4xMjgtNi4yNy4yNTRhMi4xNzIgMi4xNzIgMCAwMC0yLjAxIDEuOTggNTMuOTQ4IDUzLjk0OCAwIDAwLS4yMiA0Ljc2NmMwIDEuODI3LjEwNiAzLjQ5OC4yMiA0Ljc2NWEyLjE3MiAyLjE3MiAwIDAwMi4wMSAxLjk4YzEuNTUzLjEyNyAzLjc1Mi4yNTUgNi4yNy4yNTVzNC43MTctLjEyOSA2LjI3LS4yNTRhMi4xNzIgMi4xNzIgMCAwMDIuMDEtMS45OCA1My45MSA1My45MSAwIDAwLjIyLTQuNzY2YzAtMS44MjktLjEwNi0zLjUtLjIyLTQuNzY2YTIuMTcyIDIuMTcyIDAgMDAtMi4wMS0xLjk4QTc4LjU4IDc4LjU4IDAgMDAxMiA0Ljk5OHpNNS42MSAzLjc1NkE4MC4wNzYgODAuMDc2IDAgMDExMiAzLjQ5OGMyLjU2OCAwIDQuODA4LjEzIDYuMzkuMjU4YTMuNjcyIDMuNjcyIDAgMDEzLjM4NCAzLjM0MWMuMTE3IDEuMy4yMjYgMy4wMTYuMjI2IDQuOSAwIDEuODg1LS4xMSAzLjYwMi0uMjI2IDQuOWEzLjY3MiAzLjY3MiAwIDAxLTMuMzgzIDMuMzQyIDgwLjA0NiA4MC4wNDYgMCAwMS02LjM5MS4yNTljLTIuNTY3IDAtNC44MDgtLjEzMS02LjM5LS4yNmEzLjY3MiAzLjY3MiAwIDAxLTMuMzg0LTMuMzQgNTUuNDQ4IDU1LjQ0OCAwIDAxLS4yMjYtNC45YzAtMS44ODMuMTEtMy42LjIyNi00LjlBMy42NzIgMy42NzIgMCAwMTUuNjEgMy43NTZ6TTguNSA4LjVhMSAxIDAgMTAwIDIgMSAxIDAgMDAwLTJ6TTYgOS41YTIuNSAyLjUgMCAxMTUgMCAyLjUgMi41IDAgMDEtNSAwem04LjUwNyA0LjA1N2ExLjA1IDEuMDUgMCAwMTEuNDg1IDBsMS43MjggMS43MjhhLjc1Ljc1IDAgMDAxLjA2LTEuMDZsLTEuNzI3LTEuNzI4YTIuNTUgMi41NSAwIDAwLTMuNjA2IDBsLTIuMTcgMi4xN2EuNzQ2Ljc0NiAwIDAxLTEuMDU1IDAgMi40MzMgMi40MzMgMCAwMC0zLjQ0MS0uMDAxbC0xLjU2MSAxLjU2YS43NS43NSAwIDAwMS4wNiAxLjA2MWwxLjU2MS0xLjU2YS45MzMuOTMzIDAgMDExLjMyIDBjLjg3Ny44NzggMi4zLjg3OCAzLjE3NiAwbDIuMTctMi4xN3oiIGZpbGw9IiM5NDk5QTAiLz48L3N2Zz4=);
}

.tools-item.upload.active,
.tools-item.upload:hover {
    background-image: url(data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBjbGlwLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik0xMiA0Ljk5OGMtMi41MTggMC00LjcxNy4xMjgtNi4yNy4yNTRhMi4xNzIgMi4xNzIgMCAwMC0yLjAxIDEuOTggNTMuOTQ4IDUzLjk0OCAwIDAwLS4yMiA0Ljc2NmMwIDEuODI3LjEwNiAzLjQ5OC4yMiA0Ljc2NWEyLjE3MiAyLjE3MiAwIDAwMi4wMSAxLjk4YzEuNTUzLjEyNyAzLjc1Mi4yNTUgNi4yNy4yNTVzNC43MTctLjEyOSA2LjI3LS4yNTRhMi4xNzIgMi4xNzIgMCAwMDIuMDEtMS45OCA1My45MSA1My45MSAwIDAwLjIyLTQuNzY2YzAtMS44MjktLjEwNi0zLjUtLjIyLTQuNzY2YTIuMTcyIDIuMTcyIDAgMDAtMi4wMS0xLjk4QTc4LjU4IDc4LjU4IDAgMDAxMiA0Ljk5OHpNNS42MSAzLjc1NkE4MC4wNzYgODAuMDc2IDAgMDExMiAzLjQ5OGMyLjU2OCAwIDQuODA4LjEzIDYuMzkuMjU4YTMuNjcyIDMuNjcyIDAgMDEzLjM4NCAzLjM0MWMuMTE3IDEuMy4yMjYgMy4wMTYuMjI2IDQuOSAwIDEuODg1LS4xMSAzLjYwMi0uMjI2IDQuOWEzLjY3MiAzLjY3MiAwIDAxLTMuMzgzIDMuMzQyIDgwLjA0NiA4MC4wNDYgMCAwMS02LjM5MS4yNTljLTIuNTY3IDAtNC44MDgtLjEzMS02LjM5LS4yNmEzLjY3MiAzLjY3MiAwIDAxLTMuMzg0LTMuMzQgNTUuNDQ4IDU1LjQ0OCAwIDAxLS4yMjYtNC45YzAtMS44ODMuMTEtMy42LjIyNi00LjlBMy42NzIgMy42NzIgMCAwMTUuNjEgMy43NTZ6TTguNSA4LjVhMSAxIDAgMTAwIDIgMSAxIDAgMDAwLTJ6TTYgOS41YTIuNSAyLjUgMCAxMTUgMCAyLjUgMi41IDAgMDEtNSAwem04LjUwNyA0LjA1N2ExLjA1IDEuMDUgMCAwMTEuNDg1IDBsMS43MjggMS43MjhhLjc1Ljc1IDAgMDAxLjA2LTEuMDZsLTEuNzI3LTEuNzI4YTIuNTUgMi41NSAwIDAwLTMuNjA2IDBsLTIuMTcgMi4xN2EuNzQ2Ljc0NiAwIDAxLTEuMDU1IDAgMi40MzMgMi40MzMgMCAwMC0zLjQ0MS0uMDAxbC0xLjU2MSAxLjU2YS43NS43NSAwIDAwMS4wNiAxLjA2MWwxLjU2MS0xLjU2YS45MzMuOTMzIDAgMDExLjMyIDBjLjg3Ny44NzggMi4zLjg3OCAzLjE3NiAwbDIuMTctMi4xN3oiIGZpbGw9IiMwMEFFRUMiLz48L3N2Zz4=);
}

.bili-dyn-publishing__tools__item.emoji {
    background-image: url(data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBjbGlwLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik0zLjUgMTEuOTk4YTguNSA4LjUgMCAxMTE3IDAgOC41IDguNSAwIDAxLTE3IDB6bTguNS0xMGMtNS41MjMgMC0xMCA0LjQ3Ny0xMCAxMHM0LjQ3NyAxMCAxMCAxMCAxMC00LjQ3NyAxMC0xMC00LjQ3Ny0xMC0xMC0xMHpNNy4zMDQgOS4yNTRhLjc1Ljc1IDAgMTAtLjg5MyAxLjIwNWwxLjY2NCAxLjIzMy0xLjYuOTQ4YS43NS43NSAwIDAwLjc2NSAxLjI5bDIuNTcxLTEuNTIzYS43NS43NSAwIDAwLjA2NC0xLjI0OEw3LjMwNCA5LjI1NHptNy45OTQgNS45MzJhLjc1Ljc1IDAgMTAtMS40NTMtLjM3M2MtLjE1My41OTgtLjQ2Ny43MTQtLjYwMi43MjItLjEzNy4wMDgtLjM5NS0uMDcyLS41MjUtLjUwNGEuNzUuNzUgMCAwMC0xLjQzNiAwYy0uMTMuNDMyLS4zODguNTEyLS41MjQuNTA0LS4xMzYtLjAwOC0uNDUtLjEyNC0uNjAzLS43MjFhLjc1Ljc1IDAgMDAtMS40NTMuMzcyYy4yNzUgMS4wNzMgMS4wMzMgMS43OTMgMS45NyAxLjg0N0ExLjg4IDEuODggMCAwMDEyIDE2LjU4NmMuMzcuMzEuODMyLjQ3NSAxLjMyOS40NDcuOTM2LS4wNTQgMS42OTQtLjc3NCAxLjk2OS0xLjg0N3ptMi40NDctNS43NzZhLjc1Ljc1IDAgMDAtMS4wNDktLjE1NmwtMi41NzEgMS45MDVhLjc1Ljc1IDAgMDAuMDY0IDEuMjQ4bDIuNTcyIDEuNTI0YS43NS43NSAwIDEwLjc2NC0xLjI5bC0xLjYtLjk0OSAxLjY2NC0xLjIzM2EuNzUuNzUgMCAwMC4xNTctMS4wNDl6IiBmaWxsPSIjOTQ5OUEwIi8+PC9zdmc+);
}

.bili-dyn-publishing__tools__item.emoji {
    margin-left: 0;
}

.bili-dyn-publishing__tools__item.emoji.active,
.bili-dyn-publishing__tools__item.emoji:hover {
    background-image: url(data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBjbGlwLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik0zLjUgMTEuOTk4YTguNSA4LjUgMCAxMTE3IDAgOC41IDguNSAwIDAxLTE3IDB6bTguNS0xMGMtNS41MjMgMC0xMCA0LjQ3Ny0xMCAxMHM0LjQ3NyAxMCAxMCAxMCAxMC00LjQ3NyAxMC0xMC00LjQ3Ny0xMC0xMC0xMHpNNy4zMDQgOS4yNTRhLjc1Ljc1IDAgMTAtLjg5MyAxLjIwNWwxLjY2NCAxLjIzMy0xLjYuOTQ4YS43NS43NSAwIDAwLjc2NSAxLjI5bDIuNTcxLTEuNTIzYS43NS43NSAwIDAwLjA2NC0xLjI0OEw3LjMwNCA5LjI1NHptNy45OTQgNS45MzJhLjc1Ljc1IDAgMTAtMS40NTMtLjM3M2MtLjE1My41OTgtLjQ2Ny43MTQtLjYwMi43MjItLjEzNy4wMDgtLjM5NS0uMDcyLS41MjUtLjUwNGEuNzUuNzUgMCAwMC0xLjQzNiAwYy0uMTMuNDMyLS4zODguNTEyLS41MjQuNTA0LS4xMzYtLjAwOC0uNDUtLjEyNC0uNjAzLS43MjFhLjc1Ljc1IDAgMDAtMS40NTMuMzcyYy4yNzUgMS4wNzMgMS4wMzMgMS43OTMgMS45NyAxLjg0N0ExLjg4IDEuODggMCAwMDEyIDE2LjU4NmMuMzcuMzEuODMyLjQ3NSAxLjMyOS40NDcuOTM2LS4wNTQgMS42OTQtLjc3NCAxLjk2OS0xLjg0N3ptMi40NDctNS43NzZhLjc1Ljc1IDAgMDAtMS4wNDktLjE1NmwtMi41NzEgMS45MDVhLjc1Ljc1IDAgMDAuMDY0IDEuMjQ4bDIuNTcyIDEuNTI0YS43NS43NSAwIDEwLjc2NC0xLjI5bC0xLjYtLjk0OSAxLjY2NC0xLjIzM2EuNzUuNzUgMCAwMC4xNTctMS4wNDl6IiBmaWxsPSIjMDBBRUVDIi8+PC9zdmc+);
}

.bili-dyn-publishing__tools__item {
    background-repeat: no-repeat;
    background-size: contain;
    cursor: pointer;
    height: 24px;
    margin-left: 16px;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    width: 24px;
}

.bili-popover {
    background-color: #fff;
    border: 1px solid #e3e5e7;
    color: #222;
    font-family: PingFang SC, HarmonyOS_Regular, Helvetica Neue, Microsoft YaHei, sans-serif !important;
    font-size: 12px;
    font-weight: 400;
    margin-bottom: 10px;
    margin-top: 7px;
    pointer-events: all;
    position: absolute;
}

.bili-popover-shape {
    left: -15px;
    top: 24px;
    /* width: 384px; */
    width: 240px;
    /* min-height: 50px; */
    max-height: 240px;
    z-index: 2013;
    border-radius: 6px;
    box-shadow: rgba(0, 0, 0, 0.17) 0px 3px 5px 0px;
    cursor: default;
}

.bili-emoji {
    color: #222;
    font-family: PingFang SC, HarmonyOS_Regular, Helvetica Neue, Microsoft YaHei, sans-serif !important;
    font-size: 12px;
    font-weight: 400;
    text-align: left;
}

.bili-emoji__content {
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    /* height: 240px; */
    padding: 5px 4px 0 5px;
}

.bili-emoji__list {
    flex: 1;
    list-style: none;
    margin: 0;
    padding: 0;
}

ul {
    display: block;
    list-style-type: disc;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    padding-inline-start: 40px;
}

.bili-emoji__list__item {
    align-items: center;
    border-radius: 4px;
    cursor: pointer;
    display: inline-flex;
    justify-content: center;
    user-select: none;
}

li {
    margin: 4px;
    display: list-item;
    text-align: -webkit-match-parent;
}
</style>
  
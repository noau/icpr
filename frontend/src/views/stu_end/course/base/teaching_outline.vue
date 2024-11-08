<template> 
    <!-- 使用 VueOfficePdf 组件来显示 PDF 文件 -->
    <vue-office-pdf 
        v-if="pdf"    
        :src="pdf"
        style="height: 100vh"
        @rendered="renderedHandler"
        @error="errorHandler"
    />
    <div v-else style="text-align:center; padding-top:20px;">
        <p>未能加载 PDF 文件，请稍后再试或联系管理员。</p>
    </div>
</template>

<script>
// 引入VueOfficePdf组件和API函数
import VueOfficePdf from '@vue-office/pdf'
import { getsyllabus, getAttachmentUrl } from '@/api/course'

export default {
    components: {
        VueOfficePdf
    },
    data() {
        return {
            pdf: '' // 初始化 PDF 文件地址为空
        }
    },
    mounted () {
        this.loadSyllabus() // 页面加载时调用 loadSyllabus 方法
    },
    methods: {
        renderedHandler() {
            console.log("PDF 渲染完成")
        },
        errorHandler() {
            console.log("PDF 渲染失败")
        },
        // 获取教学大纲的附件ID并加载PDF
        loadSyllabus() {
            let courseId = 'M310006B1机械学习2024~2025上'//localStorage.getItem('courseId') // 获取课程ID
            getsyllabus(courseId).then(res => {
                console.log(res,'===')
                if (res && res.attachmentIdList && res.attachmentIdList.length > 0) {
                    const attachmentId = 47//res.attachmentIdList[0].id // 获取第一个附件的 ID
                    // 根据附件ID加载文件URL
                    this.loadFileUrl(attachmentId)
                } else {
                    console.log("未找到教学大纲附件")
                }
            }).catch(error => {
                console.log("获取教学大纲失败", error)
            })
        },
        // 根据附件ID获取文件URL
        loadFileUrl(attachmentId) {
            getAttachmentUrl(attachmentId).then(res => {
                if (res.data && res.data.url) {
                    this.pdf = res.data.url // 设置 pdf 地址为获取到的文件 URL
                    console.log("文件URL加载成功")
                } else {
                    console.log("未获取到文件URL")
                }
            }).catch(error => {
                console.log("获取文件URL失败", error)
            })
        }
    }
}
</script>

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
        <p>暂无</p>
    </div>
</template>

<script>
// 引入VueOfficePdf组件和API函数
import VueOfficePdf from '@vue-office/pdf'
import { getcalendar, getAttachmentUrl } from '@/api/course'

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
        console.log('teaching_outline出发')
        this.loadCalendar() // 页面加载时调用 loadCalendar 方法
    },
    methods: {
        renderedHandler() {
            console.log("PDF 渲染完成")
        },
        errorHandler() {
            console.log("PDF 渲染失败")
        },
        // 获取教学日历的附件ID并加载PDF
        loadCalendar() {
            let courseId = localStorage.getItem('courseId') // 获取课程ID
            getcalendar(courseId).then(res => {
                console.log(res,'===')
                if (res && res.attachmentIdList && res.attachmentIdList.length > 0) {
                    // const attachmentId = res.attachmentIdList[0].id // 获取第一个附件的 ID
                    const attachmentId = res.attachmentIdList[res.attachmentIdList.length-1].id
                    // 根据附件ID加载文件URL
                    this.loadFileUrl(attachmentId)
                } else {
                    console.log("未找到教学日历附件")
                }
            }).catch(error => {
                console.log("获取教学日历失败", error)
            })
        },
        // 根据附件ID获取文件URL
        loadFileUrl(attachmentId) {
            getAttachmentUrl(attachmentId).then(res => {
                if (res && res.url) {
                    this.pdf = res.url // 设置 pdf 地址为获取到的文件 URL
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

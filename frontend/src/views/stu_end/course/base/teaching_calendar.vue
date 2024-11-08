<template> 
    <!-- 使用 VueOfficePdf 组件来显示 PDF 文件 -->
    <vue-office-pdf 
        :src="pdf"
        style="height: 100vh"
        @rendered="renderedHandler"
        @error="errorHandler"
    />
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
        this.loadcalendar() // 页面加载时调用 loadcalendar 方法
    },
    methods: {
        renderedHandler() {
            console.log("PDF 渲染完成")
        },
        errorHandler() {
            console.log("PDF 渲染失败")
        },
        // 获取教学大纲的附件ID并加载PDF
        loadcalendar() {
            let courseId = localStorage.getItem('courseId') // 获取课程ID
            getcalendar(courseId).then(res => {
                if (res.data && res.data.attachmentIdList && res.data.attachmentIdList.length > 0) {
                    const attachmentId = res.data.attachmentIdList[0].id // 获取第一个附件的 ID
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
  
<template>
    <vue-office-pdf 
        :src="pdf"
        style="height: 100vh"
        @rendered="renderedHandler"
        @error="errorHandler"
    />
  </template>
  
  <script>
  //引入VueOfficePdf组件
  import VueOfficePdf from '@vue-office/pdf'
  import { getcalendar } from '@/api/course'
  
  export default {
    components: {
        VueOfficePdf
    },
    data() {
        return {
            pdf: 'http://static.shanhuxueyuan.com/test.pdf' //设置文档地址
        }
      },
    mounted () {
        this.getCalendar()
    },
    methods: {
        renderedHandler() {
            console.log("渲染完成")
        },
        errorHandler() {
            console.log("渲染失败")
        },
        getCalendar() {
            let id = localStorage.getItem('kcid')
            getcalendar(id).then(res => {
                console.log(res)
            })
        }
    }
  }
  </script>
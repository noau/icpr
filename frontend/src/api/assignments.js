// 作业相关接口 
import httpInstance from "@/utils/http.js"

 
 
//布置作业
export const getIssue = ({ ...data  }) =>
httpInstance.post('/assignments/issue', { ...data })


//批改作业  
export const getReviews = ({ id  }) =>
httpInstance.post('/assignments/reviews', { id })


//学生提交作业 
export const getSubmissions = ({ id  }) =>
httpInstance.post('/assignments/submissions', { id })

//互评作业 
export const getpeerReviews = ({ id  }) =>
httpInstance.post('/assignments/peer-reviews', { id })

//公布答案 
export const getissueAnswer = ({ id  }) =>
httpInstance.post('/assignments/issue-answer', { id })


//修改作业 
export const getChange = ({ ...data  }) =>
httpInstance.post('/assignments/change', { ...data })

//删除作业 
export const getDelete = ({ id  }) =>
httpInstance.delete('/assignments/delete?id='+id)

//学生获得作业列表 
export const getAssignmentsUser= ({ id  }) =>
httpInstance.get('/assignments/get-user', { id })

//老师获得批改作业列表 
export const getassignmentsTeacher = ({ id  }) =>
httpInstance.get('/assignments/get-teacher', { id })

//获得作业详情

export const getAssignmentsInfo = ({ id  }) =>
httpInstance.get('/assignments/get-info?id='+id, { id })

//获得互评列表
export const getPeerReviews = ({ id  }) =>
httpInstance.get('/assignments/peer-reviews?id='+id, { id })


//课程作业列表
export const getcourseAssignments = ({ id  }) =>
httpInstance.get('/assignments/course-assignments?id='+id, { id })

 //获得互评列表
export const getReviewList = ({ id  }) =>
httpInstance.get('/assignments/review-list?id='+id, { id })
 

 
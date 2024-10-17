//通知接口
import httpInstance from "@/utils/http.js"

// 发布评论
export const getReplies = ({ id  }) =>
httpInstance.post('/discussion/replies', { id })

//发布帖子
export const getthread = ({ id  }) =>
httpInstance.post('/discussion/thread', { id })

//班级讨论区
export const getclass = ({ id  }) =>
httpInstance.post('/discussion/class', { id })

//课程讨论区
export const getcourse = ({ id  }) =>
httpInstance.post('/discussion/course', { id })

//更改帖子信息
export const getchange = ({ id  }) =>
httpInstance.post('/discussion/change', { id })
 
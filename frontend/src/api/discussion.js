//通知接口
import httpInstance from "@/utils/http.js"

// 发布评论
export const getReplies = (data) =>
httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/discussion/replies',data)

//发布帖子
export const getthread = (data) =>
httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/discussion/thread', data)


//课程讨论区
export const getcourse = ({ id  }) =>
httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/discussion/course', { id })

//更改帖子信息
export const getchange = ({ id  }) =>
httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/discussion/change', { id })
// /user/discussion/like
export const getdiscussionlike = (data) =>
    httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/user/discussion/like', data)


// /discussion/get-thread
export const getget_thread = ({ id }) =>
    httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/discussion/get-thread?id=' + id)
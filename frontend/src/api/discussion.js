//通知接口
import httpInstance from '@/utils/http.js';

// 发布评论
export const getReplies = (data) =>
    httpInstance.post('/discussion/replies', data);

//发布帖子
export const getthread = (data) =>
    httpInstance.post('/discussion/thread', data);

//班级讨论区
export const getclass = ({id}) =>
    httpInstance.post('/discussion/class', {id});

//课程讨论区
export const getcourse = ({id}) =>
    httpInstance.get('/discussion/course', {
        params: {
            id: id,
        },
    });

//更改帖子信息
export const getchange = ({id}) =>
    httpInstance.post('/discussion/change', {id});
// /user/discussion/like
export const getdiscussionlike = (data) =>
    httpInstance.post('/user/discussion/like', data);

// /discussion/get-thread
export const getget_thread = ({id}) =>
    httpInstance.get('/discussion/get-thread?id=' + id);


// /user/discussion/delete-like
export const getdelete_like = (data) =>
    httpInstance.delete('/user/discussion/delete-like', {data});
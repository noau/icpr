import httpInstance from '@/utils/http.js';

//courses/all
// 获取课程信息
export const userCourses = (userId, token) =>
  httpInstance.get(`/courses/all?id=${userId}`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });
  

  
//courses/all-teach
  export const teacherCourses = (id, token) =>
    httpInstance.get('/courses/all-teach?id=' + id, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
  });

// /courses/all-students

export const allstudents = (id, token) =>
  httpInstance.get('/courses/all-students?id=' + id, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });

// /attachment/delete

export const deleteFile = (id, token) =>
  httpInstance.delete('attachment/delete', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
    data: {
      id: id,
    },
  });

// 确保在course.js中定义的getppt函数可以接收id参数并使用Authorization头
export const getppt = (id) =>
  httpInstance.get(`/courses/get-ppt?id=${id}`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: localStorage.getItem('token'),
    },
  });


export const getexam = (id, token) =>
  httpInstance.get('/courses/get-exam', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
    params: {
      id
    },
  });
export const getexercise = (id, token) =>
  httpInstance.get('/courses/get-exercise', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
    params: {
      id: id,
    },
  });

//   /courses/resource-ppt
export const resourceppt = (data) =>
  httpInstance.post('/courses/resource-ppt', data);

// /courses/resource-exam
export const resourceexam = (data) =>
  httpInstance.post('/courses/resource-exam', data);
// /courses/resource - exercise
export const resourcecourse = (data) =>
  httpInstance.post('/courses/resource-exercise', data);

// /courses/create-attachment-folder
export const createattachmentfolder = (data) =>
  httpInstance.post('/courses/create-attachment-folder', data);

// /courses/resource-syllabus
export const resourcesyllabus = (data) =>
  httpInstance.post('/courses/resource-syllabus', data);

// /courses/resource-calendar
export const resourcecalendar = (data) =>
  httpInstance.post('/courses/resource-calendar', data);

// /courses/get-syllabus
// export const getsyllabus = (id) =>
//   httpInstance.get('/courses/get-syllabus', {
//     params: {
//       id: id,
//     },
//   });
export const getsyllabus = (id) =>
  httpInstance.get('/courses/get-syllabus', {
    params: {
      id: id,
    },
    headers: {
      Authorization: localStorage.getItem('token') // 设置请求头的 Authorization
    }
  });

  export const getAttachmentUrl = (attachmentId) =>
    httpInstance.get(`/attachment/get?id=${attachmentId}`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: localStorage.getItem('token') // 设置请求头的 Authorization
      },
      data: { // 使用 params 传递 ID
        id: attachmentId
      },
      body: { // 使用 params 传递 ID
        id: attachmentId
      },
    });
  


// /courses/get-calendar
export const getcalendar = (id) =>
  httpInstance.get('/courses/get-calendar', {
    params: {
      id: id,
    },
  });
export const exportstudentlist = (id, token) =>
  httpInstance.post(`/courses/export-student-list?id=${id}`, {}, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });

// 上传课程简介
export const uploadCourseInfo = (file, token) =>
  httpInstance.post('/uploading/courses', file, {
    headers: {
      'Content-Type': 'multipart/form-data',
      Authorization: token,
    },
  });



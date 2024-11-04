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

export const getppt = (id, token) =>
  httpInstance.get('/courses/get-ppt?id=' + id, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });

export const getexam = (id, token) =>
  httpInstance.get('/courses/get-exam', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
    params: {
      id: id,
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

// /courses/get-syllabus
export const getsyllabus = (id) =>
  httpInstance.get('/courses/get-syllabus', {
    params: {
      id: id,
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



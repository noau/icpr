// import httpInstance from '@/utils/http.js';

// export const userCourses = (id, token) =>
//   httpInstance.get('/courses/all?id=' + id, {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: token,
//     },
//   });

// // /courses/all-students

// export const allstudents = (id, token) =>
//   httpInstance.get('/courses/all-students?id=' + id, {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: token,
//     },
//   });

// // /attachment/delete

// export const deleteFile = (id, token) =>
//   httpInstance.delete('attachment/delete', {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: token,
//     },
//     data: {
//       id: id,
//     },
//   });

// export const getppt = (id, token) =>
//   httpInstance.get('/courses/get-ppt', {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: token,
//     },
//     params: {
//       id: id,
//     },
//   });

// export const getexam = (id, token) =>
//   httpInstance.get('/courses/get-exam', {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: token,
//     },
//     params: {
//       id: id,
//     },
//   });
// export const getexercise = (id, token) =>
//   httpInstance.get('/courses/get-exercise', {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: token,
//     },
//     params: {
//       id: id,
//     },
//   });

// //   /courses/resource-ppt
// export const resourceppt = (data) =>
//   httpInstance.post('/courses/resource-ppt', data);

// // /courses/resource-exam
// export const resourceexam = (data) =>
//   httpInstance.post('/courses/resource-exam', data);
// // /courses/resource - exercise
// export const resourcecourse = (data) =>
//   httpInstance.post('/courses/resource-exercise', data);

// // /courses/create-attachment-folder
// export const createattachmentfolder = (data) =>
//   httpInstance.post('/courses/create-attachment-folder', data);

// // /courses/resource-syllabus
// export const resourcesyllabus = (data) =>
//   httpInstance.post('/courses/resource-syllabus', data);

// // /courses/get-syllabus
// export const getsyllabus = (id) =>
//   httpInstance.get('/courses/get-syllabus', {
//     params: {
//       id: id,
//     },
//   });
// // /courses/get-calendar
// export const getcalendar = (id) =>
//   httpInstance.get('/courses/get-calendar', {
//     params: {
//       id: id,
//     },
//   });
  
import httpInstance from '@/utils/http.js';

export const userCourses = (id, token) =>
  httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/all?id=' + id, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });

// /courses/all-students

export const allstudents = (id, token) =>
  httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/all-students?id=' + id, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });

// /attachment/delete

export const deleteFile = (id, token) =>
  httpInstance.delete('http://127.0.0.1:4523/m1/5175681-4840771-default/attachment/delete', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
    data: {
      id: id,
    },
  });

export const getppt = (id, token) =>
  httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/get-ppt', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
    params: {
      id: id,
    },
  });

export const getexam = (id, token) =>
  httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/get-exam', {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
    params: {
      id: id,
    },
  });
export const getexercise = (id, token) =>
  httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/get-exercise', {
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
  httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/resource-ppt', data);

// /courses/resource-exam
export const resourceexam = (data) =>
  httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/resource-exam', data);
// /courses/resource - exercise
export const resourcecourse = (data) =>
  httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/resource-exercise', data);

// /courses/create-attachment-folder
export const createattachmentfolder = (data) =>
  httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/create-attachment-folder', data);

// /courses/resource-syllabus
export const resourcesyllabus = (data) =>
  httpInstance.post('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/resource-syllabus', data);

// /courses/get-syllabus
export const getsyllabus = (id) =>
  httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/get-syllabus', {
    params: {
      id: id,
    },
  });
// /courses/get-calendar
export const getcalendar = (id) =>
  httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/courses/get-calendar', {
    params: {
      id: id,
    },
  });
  
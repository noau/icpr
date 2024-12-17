import httpInstance from '@/utils/http.js';

//courses/all
// 获取全部课程信息
export const userCourses = (userId, token) =>
  httpInstance.get(`/courses/all?id=${userId}`, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });

// 获取课程详细信息
// export const getCourseInfo = (courseId, token) => {
//   httpInstance.get(`/courses/get-info`, {
//     headers: {
//       Authorization: `Bearer ${token}`, // 设置Authorization头部
//     },
//     params: {
//       id: courseId, // 传递课程ID
//     },
//   });
// };
export const getCourseInfo = (courseId, token) => {
  return httpInstance.get('/courses/get-info', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
    params: {
      id: courseId,
    },
  });
};
//courses/all-teach
  export const teacherCourses = (id, token) =>
    httpInstance.get('/courses/all-teach?id=' + id, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
  });

// /courses/all-students

export const allstudents = (id, token) =>{
 return httpInstance.get('/courses/all-students?id=' + id, {
    headers: {
      'Content-Type': 'application/json',
      Authorization: token,
    },
  });
}
 
// 获取教师信息
// /courses/get-teacher
export const getTeacherInfo = (id, token) =>{
  return httpInstance.get('/courses/get-teacher?id=' + id, {
     headers: {
       'Content-Type': 'application/json',
       Authorization: token,
     },
   });
 }
// /attachment/delete
/**
 * 删除文件
 * @param {*} id 
 * @returns 
 */
export const deleteFile = (id) =>
  httpInstance.delete('attachment/delete', {
    // headers: {
    //   'Content-Type': 'application/json',
    //   Authorization: token,
    // },
    data: {
      id: id,
    },
  });

// 

/**
 * 删除文件夹
 * @param {} id   文件夹id
 */
export const deleteForder = (id) => {
  httpInstance.delete('/courses/delete-attachment-folder', {
    params: {
      id
    },
  });
}

export const getppt = (id, token) =>
    httpInstance.get('/courses/get-ppt', {
      headers: {
        'Content-Type': 'application/json',
        Authorization: token,
      },
      params: {
        id: id,
      },
});


// export const getexam = (id, token) =>
//   httpInstance.get('/courses/get-exam', {
//     headers: {
//       'Content-Type': 'application/json',
//       Authorization: token,
//     },
//     params: {
//       id
//     },
//   });

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
// /courses/resource-exercise
export const resourceexercise = (data) =>
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

// /courses/grade-list
// 获取成绩
export const getgrade = (id) =>
  httpInstance.get('/courses/grade-list', {
    params: {
      id: id,
    },
  });

  // /assignments/get-info
  // 获取成绩
  export const getAllgrade = (id) =>
    httpInstance.get('/assignments/get-info', {
      params: {
        id: id,
      },
    });

export const uploadselection= (file, token) =>
      httpInstance.post('/uploading/course-selection', file, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: token,
        },
      });

export const uploadstudent= (file, token) =>
        httpInstance.post('/uploading/users/student', file, {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: token,
          },
        });
export const uploadteacher = (file, token) =>
          httpInstance.post('/uploading/users/teacher', file, {
            headers: {
              'Content-Type': 'multipart/form-data',
              Authorization: token,
            },
          });

/**
 * 编辑文件夹
 * @param {} data 
 */
export const editAttachmentfolder = (data) =>{
  httpInstance.post('/courses/edit-attachment-folder', data);
}

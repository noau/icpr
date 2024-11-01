// import httpInstance from '@/utils/http.js';

// // // 登录接口
// // export const userLoginService = (loginData) => {
// //     const params = new URLSearchParams(loginData)
// //     for (let key in loginData) {
// //         params.append(key, loginData[key])
// //     }
// //     return httpInstance.post('/user/login', params)
// // }

// // 注册接口
// export const userRegisterService = ({ username, password, repassword }) =>
//   httpInstance.post('/api/reg', { username, password, repassword });

// //   登录接口
// export const userLoginService = ({ username, password }) =>
//   httpInstance.post('/user/login/teacher', { id: username, password });

// // 基本信息
// export const userGetInfoService = () => httpInstance.get('/my/userinfo');

// // 更新个人信息
// export const userUpdateInfoService = ({ id, nickname, email }) =>
//   httpInstance.put('/my/userinfo', { id, nickname, email });
// // 上传头像
// export const userUploadAvatarService = (avatar) =>
//   httpInstance.patch('/my/update/avatar', { avatar });
// // 更新密码信息
// export const userUpdatePassService = ({ old_pwd, new_pwd, re_pwd }) =>
//   httpInstance.patch('/my/updatepwd', { old_pwd, new_pwd, re_pwd });

// //修改用户信息
// export const userChange_info = ({ old_pwd, new_pwd, re_pwd }) =>
//   httpInstance.post('/user/change-info', { old_pwd, new_pwd, re_pwd });

// //获取用户信息
// export const userUserInfo = ({ id }) =>
//   httpInstance.get('/user/info?id=' + id, { id });

// //修改/找回密码
// export const userchange_password = ({ id, password, newPassword }) =>
//   httpInstance.post('/user/change-password', { id, password, newPassword });

// //找回密码 邮箱
// export const resetPwdEmail = ({ id, email, idCardNumber }) =>
//   httpInstance.post('/user/reset-pwd-email', { id, email, idCardNumber });

// //找回密码 手机号
// export const getResetPwdphone = ({ id, phoneNumber, idCardNumber }) =>
//   httpInstance.post('/user/reset-pwd-phone', { id, phoneNumber, idCardNumber });

// //上传用户头像
// export const setAvatar = ({ id, avatar }) =>
//   httpInstance.post('/user/avatar', { id, avatar });

// //获取所有粉丝
// export const userFollowers = ({ id }) =>
//   httpInstance.get('/user/followers?id=' + id, { id });

// //获取你所有的关注者
// export const userSubscriptions = ({ id }) =>
//   httpInstance.get('/user/subscriptions?id=' + id, { id });

// //获取所有收藏夹
// export const userFolders = ({ id }) =>
//   httpInstance.get('/user/folders?id=' + id, { id });

// //获取指定收藏夹内容
// export const userFavorites = ({ id }) =>
//   httpInstance.get('/user/favorites?id=' + id, { id });

// //创建收藏夹
// export const userCreate_folder = ({ userId, name, createdAt, isPrivate }) =>
//   httpInstance.post('/user/create-folder', {
//     userId,
//     name,
//     createdAt,
//     isPrivate,
//   });

// //收藏帖子
// export const userCreate_favorite = ({ userId, threadId, folderId }) =>
//   httpInstance.post('/user/create-favorite', { userId, threadId, folderId });

// //点赞
// export const discussionlike = ({ id }) =>
//   httpInstance.post('/user/discussion/like', { id });

// //取消关注
// export const getDeleteSubscription = ({ id }) =>
//   httpInstance.post('/user/delete-subscription', { id });
// //取消关注
// export const getdDiscussionlike = ({ id }) =>
//   httpInstance.post('/user/make-subscription', { id });

// // /user/delete-favorite
// export const userDelete_favorite = ({ id }) =>
//   httpInstance.delete('/user/delete-favorite', { id });

// // /user/delete-folder
// export const userDelete_folder = (data) => {
//   console.log(data);

//   return httpInstance({
//     url: '/user/delete-folder',
//     method: 'delete',
//     data: data
//   })
// };

// // /user/change-folder
// export const userChange_folder = ({ id, name, isPrivate }) =>
//   httpInstance.post('/user/change-folder', { id, name, isPrivate });



import httpInstance from '@/utils/http.js';

// Register interface
export const userRegisterService = ({ username, password, repassword }) =>
  httpInstance.post('/api/reg', { username, password, repassword });

// Dynamic Login interface (student, teacher, or administrator)
export const userLoginService = ({ username, password, role }) => {
  console.log(role);
  let endpoint = '/user/login/';
  if (role === 'student') {
    endpoint += 'student';
  } else if (role === 'teacher') {
    endpoint += 'teacher';
  } else if (role === 'administrator') {
    endpoint += 'administrator';
  }
  return httpInstance.post(endpoint, { id: username, password });
};

// Basic information retrieval
export const userGetInfoService = () => httpInstance.get('/my/userinfo');

// Update personal information
export const userUpdateInfoService = ({ id, nickname, email }) =>
  httpInstance.put('/my/userinfo', { id, nickname, email });

// Upload avatar
export const userUploadAvatarService = (avatar) =>
  httpInstance.patch('/my/update/avatar', { avatar });

// Update password information
export const userUpdatePassService = ({ old_pwd, new_pwd, re_pwd }) =>
  httpInstance.patch('/my/updatepwd', { old_pwd, new_pwd, re_pwd });

// Change user information
export const userChange_info = ({ old_pwd, new_pwd, re_pwd }) =>
  httpInstance.post('/user/change-info', { old_pwd, new_pwd, re_pwd });

// Retrieve user information
export const userUserInfo = ({ id }) =>
  httpInstance.get(`/user/info?id=${id}`);

// Change/recover password
export const userchange_password = ({ id, password, newPassword }) =>
  httpInstance.post('/user/change-password', { id, password, newPassword });

// Recover password via email
export const resetPwdEmail = ({ id, email, idCardNumber }) =>
  httpInstance.post('/user/reset-pwd-email', { id, email, idCardNumber });

// Recover password via phone number
export const getResetPwdphone = ({ id, phoneNumber, idCardNumber }) =>
  httpInstance.post('/user/reset-pwd-phone', { id, phoneNumber, idCardNumber });

// Upload user avatar
export const setAvatar = ({ id, avatar }) =>
  httpInstance.post('/user/avatar', { id, avatar });

// Get all followers
export const userFollowers = ({ id }) =>
  httpInstance.get(`/user/followers?id=${id}`);

// Get all subscriptions/followers
export const userSubscriptions = ({ id }) =>
  httpInstance.get(`/user/subscriptions?id=${id}`);

// Get all folders
export const userFolders = ({ id }) =>
  httpInstance.get(`/user/folders?id=${id}`);

// Get specified folder contents
export const userFavorites = ({ id }) =>
  httpInstance.get(`/user/favorites?id=${id}`);

// Create a new folder
export const userCreate_folder = ({ userId, name, createdAt, isPrivate }) =>
  httpInstance.post('/user/create-folder', { userId, name, createdAt, isPrivate });

// Save a post to a folder
export const userCreate_favorite = ({ userId, threadId, folderId }) =>
  httpInstance.post('/user/create-favorite', { userId, threadId, folderId });

// Like a discussion
export const discussionlike = ({ id }) =>
  httpInstance.post('/user/discussion/like', { id });

// Unsubscribe a user
export const getDeleteSubscription = ({ id }) =>
  httpInstance.post('/user/delete-subscription', { id });

// Subscribe a user
export const getdDiscussionlike = ({ id }) =>
  httpInstance.post('/user/make-subscription', { id });

// Delete a favorite post
export const userDelete_favorite = ({ id }) =>
  httpInstance.delete('/user/delete-favorite', { id });

// Delete a folder
export const userDelete_folder = (data) =>
  httpInstance({
    url: '/user/delete-folder',
    method: 'delete',
    data: data
  });

// Change a folder's information
export const userChange_folder = ({ id, name, isPrivate }) =>
  httpInstance.post('/user/change-folder', { id, name, isPrivate });

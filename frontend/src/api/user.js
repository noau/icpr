import httpInstance from '@/utils/http.js';

// // 登录接口
// export const userLoginService = (loginData) => {
//     const params = new URLSearchParams(loginData)
//     for (let key in loginData) {
//         params.append(key, loginData[key])
//     }
//     return httpInstance.post('/user/login', params)
// }

// 注册接口
export const userRegisterService = ({ username, password, repassword }) =>
  httpInstance.post('/api/reg', { username, password, repassword });

//   登录接口
export const userLoginService = ({ username, password }) =>
  httpInstance.post('/user/login', { id: username, password });

// 基本信息
export const userGetInfoService = () => httpInstance.get('/my/userinfo');

// 更新个人信息
export const userUpdateInfoService = ({ id, nickname, email }) =>
  httpInstance.put('/my/userinfo', { id, nickname, email });
// 上传头像
export const userUploadAvatarService = (avatar) =>
  httpInstance.patch('/my/update/avatar', { avatar });
// 更新密码信息
export const userUpdatePassService = ({ old_pwd, new_pwd, re_pwd }) =>
  httpInstance.patch('/my/updatepwd', { old_pwd, new_pwd, re_pwd });

//修改用户信息
export const userChange_info = ({ old_pwd, new_pwd, re_pwd }) =>
  httpInstance.post('/user/change_info', { old_pwd, new_pwd, re_pwd });

//获取用户信息
export const userUserInfo = ({ id }) =>
  httpInstance.get('/user/info?id=' + id, { id });

//修改/找回密码
export const userchange_password = ({ id, password, newPassword }) =>
  httpInstance.post('/user/change_password', { id, password, newPassword });

//找回密码 邮箱
export const resetPwdEmail = ({ id, email, idCardNumber }) =>
  httpInstance.post('/user/reset-pwd-email', { id, email, idCardNumber });

//找回密码 手机号
export const getResetPwdphone = ({ id, phoneNumber, idCardNumber }) =>
  httpInstance.post('/user/reset-pwd-phone', { id, phoneNumber, idCardNumber });

//上传用户头像
export const setAvatar = ({ id, avatar }) =>
  httpInstance.post('/user/avatar', { id, avatar });

//获取所有粉丝
export const userFollowers = ({ id }) =>
  httpInstance.get('/user/followers?id=' + id, { id });

//获取你所有的关注者
export const userSubscriptions = ({ id }) =>
  httpInstance.get('/user/subscriptions?id=' + id, { id });

//获取所有收藏夹
export const userFolders = ({ id }) =>
  httpInstance.get('/user/folders?id=' + id, { id });

//获取指定收藏夹内容
export const userFavorites = ({ id }) =>
  httpInstance.get('/user/favorites?id=' + id, { id });

//创建收藏夹
export const userCreate_folder = ({ userId, name, createdAt, isPrivate }) =>
  httpInstance.post('/user/create-folder', {
    userId,
    name,
    createdAt,
    isPrivate,
  });

//收藏帖子
export const userCreate_favorite = ({ userId, threadId, folderId }) =>
  httpInstance.post('/user/create-favorite', { userId, threadId, folderId });

//点赞
export const discussionlike = ({ id }) =>
  httpInstance.post('/user/discussion/like', { id });

//取消关注
export const getDeleteSubscription = ({ id }) =>
  httpInstance.post('/user/delete-subscription', { id });
//取消关注
export const getdDiscussionlike = ({ id }) =>
  httpInstance.post('/user/make-subscription', { id });

// /user/delete-favorite
export const userDelete_favorite = ({ id }) =>
  httpInstance.delete('/user/delete-favorite', { id });

// /user/delete-folder
export const userDelete_folder = ({ id }) =>
  httpInstance.delete('/user/delete-folder', { id });

// /user/change-folder
export const userChange_folder = ({ id, name, isPrivate }) =>
  httpInstance.post('/user/change-folder', { id, name, isPrivate });

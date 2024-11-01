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

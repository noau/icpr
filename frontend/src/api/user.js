import httpInstance from "@/utils/http.js"

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
    httpInstance.post('/api/reg', { username, password, repassword })
  
 //   登录接口
 export const userLoginService = ({ username, password }) =>
    httpInstance.post('/user/login', { "id": username, password })
  
  // 基本信息
  export const userGetInfoService = () => httpInstance.get('/my/userinfo')
  
  // 更新个人信息
  export const userUpdateInfoService = ({ id, nickname, email }) =>
    httpInstance.put('/my/userinfo', { id, nickname, email })
  // 上传头像
  export const userUploadAvatarService = (avatar) =>
    httpInstance.patch('/my/update/avatar', { avatar })
  // 更新密码信息
  export const userUpdatePassService = ({ old_pwd, new_pwd, re_pwd }) =>
    httpInstance.patch('/my/updatepwd', { old_pwd, new_pwd, re_pwd })
  
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
export const userRegisterService = ({username, password, repassword}) =>
    httpInstance.post('/api/reg', {username, password, repassword})

//   登录接口
export const userLoginService = ({username, password}) =>
    httpInstance.post('/user/login', {"id": username, password})

// 基本信息
export const userGetInfoService = () => httpInstance.get('/my/userinfo')

// 更新个人信息
export const userUpdateInfoService = ({id, nickname, email}) =>
    httpInstance.put('/my/userinfo', {id, nickname, email})

// 上传用户头像
export const setAvatar = ({ id, avatar }) =>
    httpInstance.post('/user/avatar', { id, avatar });

// 更新密码信息
export const userUpdatePassService = ({old_pwd, new_pwd, re_pwd}) =>
    httpInstance.patch('/my/updatepwd', {old_pwd, new_pwd, re_pwd})

//修改用户信息
export const userChange_info = ({old_pwd, new_pwd, re_pwd, token}) =>
    httpInstance.post('/user/change_info', {old_pwd, new_pwd, re_pwd}, {
        headers: {
            'Authorization': token,
        }
    })


//获取用户信息
export const userUserInfo = ({id, token}) =>
    httpInstance.get('/user/info?id=' + id, {
        headers: {
            'Authorization': token,
        }
    })


//修改/找回密码
export const userchange_password = ({id, password, newPassword, token}) =>
    httpInstance.post('/user/change_password', {id, password, newPassword}, {
        headers: {
            'Authorization': token,
        }
    })

//找回密码 邮箱
export const resetPwdEmail = ({data}) =>
    httpInstance.post('/reset-pwd-email', {data})


//找回密码 手机号
export const userChangePassword = ({}) =>
    httpInstance.post('/user/reset-pwd-phone', {})


//获取所有粉丝
export const userFollowers = ({id, token}) =>
    httpInstance.get('/user/followers?id=' + id, {
        headers: {
            'Authorization': token,
        }
    })


//获取你所有的关注者
export const userSubscriptions = ({id, token}) =>
    httpInstance.get('/user/subscriptions?id=' + id, {
        headers: {
            'Authorization': token,
        }
    })

//获取所有收藏夹
export const userFolders = ({id, token}) =>
    httpInstance.get('/user/folders?id=' + id, {
        headers: {
            'Authorization': token,
        }
    })

//获取指定收藏夹内容
export const userFavorites = ({id, token}) =>
    httpInstance.get('/user/favorites?id=' + id, {
        headers: {
            'Authorization': token,
        }
    })

//创建收藏夹
export const userCreate_folder = ({userId, name, createdAt, isPrivate, token}) =>
    httpInstance.post('/user/create-folder', {userId, name, createdAt, isPrivate}, {
        headers: {
            'Authorization': token,
        }
    })


//收藏帖子
export const userCreate_favorite = ({id, token}) =>
    httpInstance.post('/user/create-favorite', {id}, {
        headers: {
            'Authorization': token,
        }
    })

//点赞
export const discussionlike = ({id, token}) =>
    httpInstance.post('/user/discussion/like', {id}, {
        headers: {
            'Authorization': token,
        }
    })
 
 
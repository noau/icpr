import httpInstance from "@/utils/http.js"

// 登录接口
export const userLoginService = (loginData) => {
    const params = new URLSearchParams(loginData)
    for (let key in loginData) {
        params.append(key, loginData[key])
    }
    return httpInstance.post('/user/login', params)
}
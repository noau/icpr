// // axios基础的封装
// import axios from 'axios';

// const baseURL = '/api';
// const httpInstance = axios.create({
//   baseURL
// })

// //拦截器
// //1.请求拦截器
// httpInstance.interceptors.request.use(config => {
//     return config
// }, e => Promise.reject(e))
// //2.响应拦截器
// httpInstance.interceptors.response.use(res => res.data, e => {
//     return Promise.reject(e)
// })

// export default httpInstance

import axios from 'axios'
import { useUserStore } from '@/stores/user.js'
import router from '@/router'

// const baseURL = 'http://big-event-vue-api-t.itheima.net'
const baseURL = 'http://localhost:8080/'
// const baseURL = 'https://apifoxmock.com/m1/5410209-5084126-default'

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000
})

instance.interceptors.request.use(
  (config) => {
    // TODO 2. 携带token
    const userStore = useUserStore(); 
    if (userStore.token || localStorage.getItem('token')) {
      config.headers.Authorization = userStore.token || localStorage.getItem('token')
    }
    return config
  },
  (err) => Promise.reject(err)
)

instance.interceptors.response.use(
  (res) => {
    // TODO 3. 处理业务失败
    // TODO 4. 摘取核心响应数据
    if (res.data.code === 0 || res.status === 200) {
      return res.data
    }
    // 处理业务失败，给错误提示，抛出错误
    alert(res.data.message || '服务异常')
    return Promise.reject(res.data)
  },
  (err) => {
    // TODO 5. 处理401错误
    // 错误的特殊情况 => 401权限不足 或 token 过期 => 拦截登录
    if (err.response?.status === 401) {
      // router.push('/login')
    }
    // 错误的默认情况 => 只要给提示
    alert(err.response.data.message || '服务异常')
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }

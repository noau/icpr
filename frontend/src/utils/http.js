// axios基础的封装
import axios from 'axios';

const baseURL = '/api';
const httpInstance = axios.create({
  baseURL
})

//拦截器
//1.请求拦截器
httpInstance.interceptors.request.use(config => {
    return config
}, e => Promise.reject(e))
//2.响应拦截器
httpInstance.interceptors.response.use(res => res.data, e => {
    return Promise.reject(e)
})

export default httpInstance
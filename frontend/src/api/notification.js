//通知接口
import httpInstance from "@/utils/http.js"

// 获得通知列表
export const notificationsGet = ({ id  }) =>
httpInstance.get('/notifications/get', { id })

//发布通知
export const notificationsPublish = ({ id  }) =>
httpInstance.get('/notifications/publish', { id })
 
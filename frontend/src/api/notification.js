//通知接口
import httpInstance from "@/utils/http.js"

// 获得通知列表
export const notificationsGet = ({ id  }) =>
httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/notifications/get', { id })

//发布通知
export const notificationsPublish = ({ id  }) =>
httpInstance.get('http://127.0.0.1:4523/m1/5175681-4840771-default/notifications/publish', { id })
 
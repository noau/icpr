//通知接口
import httpInstance from "@/utils/http.js"

// 获得通知列表
export const notificationsGet = ({ id  }) =>
    httpInstance.get('/notifications/get?id=' + id)

// 更新通知
export const updateCollectionNotification = (notificationData) =>
    httpInstance.post('/notifications/star', notificationData);

// 更新通知
export const updateReadNotification = (notificationData) =>
    httpInstance.post('/notifications/read', notificationData);

//删除通知
export const deleteSignalNotification = (id) =>
    httpInstance.delete(`/notifications/delete?id=${id}`); // 通过查询参数传递ID

// 获得收藏通知列表
export const collectionNotificationsGet = ({ id  }) =>
    httpInstance.get('/notifications/get-all-collection?id=' + id)

// 获得指定类型的收藏通知列表
export const typeNotificationsByTypeGet = ({ id, type }) =>
    httpInstance.get(`/notifications/get-notification-by-type?id=${id}&type=${type}`);

// 发布通知
export const publishNotification = (notificationData) =>
    httpInstance.post('/notifications/publish', notificationData);

//通知接口
import httpInstance from "@/utils/http.js"

// 获得通知列表
export const notificationsGet = ({ id  }) =>
    httpInstance.get('/notifications/get?id=' + id)

export const updateCollectionNotification = (notificationData) =>
    httpInstance.post('/notifications/star', notificationData);


// 更新通知
export const updateReadNotification = (notificationData) =>
    httpInstance.post('/notifications/read', notificationData);

//删除通知
export const deleteSignalNotification = (id) =>
    httpInstance.delete(`/notifications/delete?id=${id}`); // 通过查询参数传递ID


//发布通知
export const notificationsPublish = ({
                                         userId,
                                         type,
                                         content,
                                         isRead,
                                         createdAt,
                                         relatedId,
                                         triggeredBy,
                                         courseId,
                                         isStar
                                     }) => {
    return httpInstance.post('/notifications/publish', {
        userId,
        type,
        content,
        isRead,
        createdAt,
        relatedId,
        triggeredBy,
        courseId,
        isStar
    });
};

 
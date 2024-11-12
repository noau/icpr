
import httpInstance from "@/utils/http.js"
//上传附件
export const upload = (id, token) =>
    httpInstance.post('/attachment/upload',  {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token,
        }
    })

  export const getAttachmentUrl = (attachmentId) =>
    httpInstance.get(`/attachment/get?id=${attachmentId}`, {
      headers: {
        'Content-Type': 'application/json',
        Authorization: localStorage.getItem('token') // 设置请求头的 Authorization
      },
      data: { // 使用 params 传递 ID
        id: attachmentId
      },
      body: { // 使用 params 传递 ID
        id: attachmentId
      },
    });

import httpInstance from "@/utils/http.js"
//上传附件
export const upload = (id, token) =>
    httpInstance.post('/attachment/upload',  {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token,
        }
    })
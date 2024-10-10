import httpInstance from "@/utils/http.js"

export const userCourses = (id, token) =>
    httpInstance.get('/courses/all?id=' + id,  {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token,
        }
    })

import httpInstance from "@/utils/http.js"

export const userCourses = (id, token) =>
    httpInstance.get('/courses/all?id=' + id,  {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token,
        }
    })


    // /courses/all-students

export const allstudents = (id, token) =>
    httpInstance.get('/courses/all-students?id=' + id,  {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token,
        }
    })


    // /attachment/delete
    
export const deleteFile = (id, token) =>
    httpInstance.delete('attachment/delete',  {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': token,
        },
        data: {
            id: id
        }
    })
    
    export const getppt = (id, token) =>
        httpInstance.get('/courses/get-ppt',  {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': token,
            },
            params: {
                id: id
            }
           
        })


   




    export const getexam = (id, token) =>
        httpInstance.get('/courses/get-exam',  {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token,
                },
                params: {
                    id: id
                }
               
            })
            export const getexercise = (id, token) =>
                httpInstance.get('/courses/get-exercise',  {
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': token,
                        },
                        params: {
                            id: id
                        }
                       
                    })
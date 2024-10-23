import httpInstance from "@/utils/http.js"
// /assignments/submissions
export const getSubmissions = (data) =>
    httpInstance.post('/assignments/submissions', data)

// /assignments/course-assignments
export const getCourseAssignment = (id) =>
    httpInstance.get('/assignments/course-assignments?id=' + id)

export const getAssignmentsInfo = (id) =>
    httpInstance.get('/assignments/get-info?id=' + id)
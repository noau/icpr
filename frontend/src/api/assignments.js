// 作业相关接口 
import httpInstance from "@/utils/http.js";

// 布置作业
export const issueAssignment = (assignment) =>
    httpInstance.post('/assignments/issue', assignment);

// 批改作业
export const reviewAssignment = (assignmentReview) =>
    httpInstance.post('/assignments/reviews', assignmentReview);

// 学生提交作业
export const submitAssignment = (submission) =>
    httpInstance.post('/assignments/submissions', submission);

// 互评作业
export const peerReviewAssignment = (peerReview) =>
    httpInstance.post('/assignments/peer-reviews', peerReview);

// 公布答案
export const issueAnswer = (answer) =>
    httpInstance.post('/assignments/issue-answer', answer);

// 修改作业
export const changeAssignment = (assignment) =>
    httpInstance.post('/assignments/change', assignment);

// 删除作业
export const deleteAssignment = (id) =>
    httpInstance.delete('/assignments/delete', { params: { id } });

// 获得作业列表
export const getCourseAssignments = (courseId) =>
    httpInstance.get('/assignments/course-assignments', { params: { id: courseId } });

// 获得作业详情
export const getAssignmentDescription = (id) =>
    httpInstance.get('/assignments/get-info', { params: { id } });

// 获得作业提交列表
export const getAssignmentSubmissions = (assignmentId) =>
    httpInstance.get('/assignments/review-list', { params: { id: assignmentId } });
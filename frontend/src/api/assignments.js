// 作业相关接口
import httpInstance from "@/utils/http.js";

// 获取课程列表
export const getAllTeachCourses = ({ id }) =>
  httpInstance.get(`/courses/all-teach`, { params: { id } });

//布置作业
export const getIssue = (data) => httpInstance.post("/assignments/issue", data);

//学生端课程作业列表
export const getcourseAssignmentsStudent = (data) =>
  httpInstance.get("/assignments/course-assignments/student", { params: { ...data } });

//学生提交作业
export const getSubmissions = (data) =>
  httpInstance.post("/assignments/submissions", data);

// View specific assignment submission by assignmentId and studentId
export const viewSubmission = ({ assignmentId, studentId }) =>
  httpInstance.get(`/assignments/submission`, {
    params: { assignmentId, studentId }
  });


//互评作业列表
export const getPeerReviewList = (data) =>
  httpInstance.get("/assignments/peer-review-list", { params: { ...data } });

//互评作业
export const getpeerReviews = ({ id }) =>
  httpInstance.post("/assignments/peer-reviews", { id });

//添加互评作业
export const addPeerReviews = (data) =>
  httpInstance.post("/assignments/peer-reviews", data);

//公布答案
export const getissueAnswer = ({ id }) =>
  httpInstance.post("/assignments/issue-answer", { id });

//修改作业
export const getChange = ({ ...data }) =>
  httpInstance.post("/assignments/change", { ...data });

//删除作业
export const getDelete = ({ id }) =>
  httpInstance.delete("/assignments/delete?id=" + id);



//获得作业详情

export const getAssignmentsInfo = ({ id }) =>
  httpInstance.get("/assignments/get-info?id=" + id, { id });

export const getAssignmentsDetail = ({ id }) =>
  httpInstance.get("/assignments/get-detail?id=" + id, { id });


//获得互评列表
export const getPeerReviews = ({ id }) =>
  httpInstance.get("/assignments/peer-reviews?id=" + id, { id });

//教师端课程作业列表
export const getcourseAssignments = ({ id }) =>
  httpInstance.get("/assignments/course-assignments?id=" + id, { id });

//教师获得批改作业列表
export const getReviewList = ({ id }) =>
  httpInstance.get("/assignments/review-list?id=" + id, { id });

// 批改作业
export const submitReview = ({ submissionId, grade, feedback, gradedAt }) =>
  httpInstance.post("/assignments/reviews", {
    submissionId,
    grade,
    feedback,
    gradedAt,
  });

<template>
  <div class="homework-list">
    <el-row :gutter="20" class="header" style="margin-top: -25px; margin-bottom: 2px">
      <el-col :span="8">
        <el-form :inline="true" :model="form">
          <el-form-item label="">
            <el-input v-model="form.title" placeholder="请输入作业标题" class="search-input"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" style="margin-right: 100px; margin-left: -80px; padding: 10px" @click="search">
          搜索
        </el-button>
      </el-col>
      <el-col :span="4">
        <el-button round type="primary" @click="goToAssignHomework">布置作业</el-button>
      </el-col>
    </el-row>

    <div class="box-card">
      <el-card>
        <div class="forum-list" id="data" style="max-height: 500px; overflow-y: auto">
          <el-table :data="paginatedData" style="width: 100%" class="homework-table">
            <el-table-column prop="title" label="作业标题" width="350" align="center" header-align="center">
              <template #default="scope">
                <div>{{ scope.row.title }}</div>
                <div class="gray-text">提交起止时间：{{ scope.row.start }} - {{ scope.row.end }}</div>
              </template>
            </el-table-column>

            <el-table-column label="提交人数" width="150" align="center" header-align="center">
              <template #default="scope">{{ scope.row.submitted }}/{{ scope.row.submitTotal }}</template>
            </el-table-column>

            <el-table-column label="批阅作业" width="150" align="center" header-align="center">
              <template #default="scope">
                <el-icon class="custom-icon" @click="goToSubmissonCondition(scope.row)">
                  <EditPen />
                </el-icon>
              </template>
            </el-table-column>

            <el-table-column label="成绩统计" width="150" align="center" header-align="center">
              <template #default="scope">
                <el-icon class="custom-icon" @click="goToHomeworkStatistics(scope.row)">
                  <TrendCharts />
                </el-icon>
              </template>
            </el-table-column>

            <el-table-column label="操作" width="250" align="center" header-align="center">
              <template #default="scope">
                <!-- <el-button type="text" @click="publishHomework(scope.row)">公开作业</el-button> -->
                <!-- <el-button type="text" @click="publishGrades(scope.row)">公布成绩</el-button> -->
                <!-- <el-button type="text" @click="publishAnswers(scope.row)">公布答案</el-button> -->
                <el-button type="text" @click="editHomework(scope.row)">修改</el-button>
                <el-button type="text" @click="deleteHomework(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-container">
          <el-pagination layout="prev, pager, next" :total="filteredHomeworkList.length" :page-size="pageSize" :current-page="currentPage" @current-change="handleCurrentChange" />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useCourseStore } from "@/stores/course";
import { getcourseAssignments, getDelete,getChange } from "@/api/assignments";
import { ElMessage, ElMessageBox } from "element-plus";

const router = useRouter();
const form = ref({ title: "" });
const homeworkList = ref([]);
const pageSize = ref(8);
const currentPage = ref(1);
const searchQuery = ref("");
const courseStore = useCourseStore();

const filteredHomeworkList = computed(() => {
  if (!searchQuery.value) {
    return homeworkList.value;
  }
  return homeworkList.value.filter((homework) =>
    homework.title.includes(searchQuery.value)
  );
});

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  return filteredHomeworkList.value.slice(start, start + pageSize.value);
});

const search = () => {
  searchQuery.value = form.value.title;
  currentPage.value = 1;
};

const publishHomework = async (row) => {
  console.log("公开作业:", row);

  let data = await getChange({...row,isPrivate:1,attachments:[]})
  console.log(data);
  
  alert("发布成功！");
};

const publishGrades = async (row) => {
  console.log("公开作业:", row);

let data = await getChange({...row,publishGrade:1,attachments:[]})
console.log(data);
  alert("公布成功！");
};

const publishAnswers = async (row) => {
  console.log("公布答案:", row);
  alert("公布成功！");
};

const editHomework = (row) => {
  router.push({
    path: "/tea-end/course/examine/assign-homework",
    query: { id: row.id },
  });
};

// const deleteHomework = async (row) => {
//   try {
//     await ElMessageBox.confirm(`确定要删除作业 "${row.title}" 吗？`, "删除确认", {
//       confirmButtonText: "删除",
//       cancelButtonText: "取消",
//       type: "warning",
//     });
//     // 执行删除操作
//     await getDelete({ id: row.id });
//     ElMessage.success("删除成功！");
//     // 刷新作业列表
//     getcourseAssignment();
//   } catch (error) {
//     // 用户取消了删除操作或删除失败
//     if (error !== "cancel") {
//       console.error("删除作业时出错:", error);
//       ElMessage.error("删除失败，请稍后重试。");
//     }
//   }
// };
const deleteHomework = async (row) => {
  await ElMessageBox.confirm(`确定要删除作业 "${row.title}" 吗？`, "删除确认", {
    confirmButtonText: "删除",
    cancelButtonText: "取消",
    type: "warning",
  });
  // 执行删除操作
  await getDelete({ id: row.id });
    
  // 检查删除请求的返回结果是否成功
  alert("删除成功！");
  // 刷新作业列表
  await getcourseAssignment();
};

const goToAssignHomework = () => {
  router.push({ path: "/tea-end/course/examine/assign-homework" });
};

const goToSubmissonCondition = (row) => {
  router.push({
    path: "/tea-end/course/examine/grade-homework",
    query: { title: row.title, start: row.start, end: row.end, id: row.id },
  });
};

const goToHomeworkStatistics = (row) => {
  console.log(row);
  
  router.push({ path: `/tea-end/course/examine/homework-statistics`,query:{id:row.id} });
};

function handleCurrentChange(page) {
  currentPage.value = page;
}

const getcourseAssignment = async () => {
  let courseId = courseStore.getCourseId;

  // 如果 courseStore 中没有 courseId，从 localStorage 获取
  if (!courseId) {
    courseId = localStorage.getItem("courseId");
    if (courseId) {
      courseStore.setCourseId(courseId); // 存储到 Pinia store 中
    }
  }

  if (courseId) {
    const res = await getcourseAssignments({ id: courseId });
    homeworkList.value = res || [];
  } else {
    console.error("课程 ID 为空，请确保课程 ID 已正确设置");
  }
};

onMounted(async () => {
  console.log("当前课程 ID:", courseStore.getCourseId); // 输出课程ID，检查是否为空
  getcourseAssignment();
});


</script>

<style scoped>
.forum-list {
  padding: 0;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.box-card {
  margin: 20px;
  margin-top: -2px;
}

.el-button {
  font-size: 14px;
  padding: 8px 10px;
}

.search-input {
  flex: 1;
  width: 300px;
  margin-left: 20px;
}

.gray-text {
  color: gray;
  font-size: 12px;
}

.custom-icon {
  font-size: 18px;
}
</style>


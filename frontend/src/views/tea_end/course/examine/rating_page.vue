<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <back_header/>
      </el-header>
      <el-main>
        <div class="grading-container"> 
          <!-- Table -->
          <div class="custom-table">
            <el-row>
              <el-col :span="23" style="margin-left: -40px; margin-right: -15px; margin-top: -40px;">
                <el-table :data="transformedData" border style="width: 100%" :show-header="false">
                  <el-table-column prop="label" width="100">
                    <template #default="scope">
                      <strong>{{ scope.row.label }}</strong>
                    </template>
                  </el-table-column>
                  <el-table-column
                    v-for="(item, index) in tableData"
                    :key="index"
                    :prop="`value${index}`"
                    width="90"
                  >
                    <template #default="scope">
                      <div :class="{'highlight': currentStudentIndex === index}">
                        {{ scope.row.label === '得分' && currentStudentIndex === index ? '正在批阅...' : scope.row[`value${index}`] }}
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </el-col>
              <el-col :span="1" class="button-col" style="margin-left: 40px;  margin-top: -40px;">
                <el-button round type="primary" style="margin-bottom: 10px; margin-right: 15px; padding: 7px;" @click="previousStudent">上一个</el-button>
                <el-button round style="margin-right: 25px; padding: 7px;" @click="nextStudent">下一个</el-button>
              </el-col>
            </el-row>
          </div>

          <!-- 评分区域 -->
          <el-row :gutter="20" class="content" style="margin-top: -30px;">
            <el-col :span="16">
              <el-card>
                <iframe src="path/to/your/pdf" width="100%" height="500px"></iframe>
              </el-card>
            </el-col>
            <el-col :span="8">
              <el-card>
                <el-form :model="gradingForm">
                  <el-form-item label="成绩" required>
                    <el-input-number v-model="gradingForm.grade" :min="0" :max="10"></el-input-number>
                  </el-form-item>
                  <el-form-item label="评语" label-position="top">
                    <v-md-editor v-model="gradingForm.comment" height="200px"></v-md-editor>
                  </el-form-item>
                  <el-form-item style="text-align: right;">
                    <el-button round type="primary" @click="submitGrading">确定</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import back_header from '@/components/back_header.vue';

// 使用 github 主题
VMdEditor.use(githubTheme);

const tableData = ref([
  { name: '李', score: 87 },
  { name: '张周周张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: '正在批阅...' },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
  { name: '李', score: 87 },
  { name: '张', score: 91 },
  { name: '官', score: 90 },
]);

const transformedData = ref([
  { label: '姓名', ...tableData.value.reduce((acc, item, index) => ({ ...acc, [`value${index}`]: item.name }), {}) },
  { label: '得分', ...tableData.value.reduce((acc, item, index) => ({ ...acc, [`value${index}`]: item.score }), {}) },
]);

const currentStudentIndex = ref(15);

const gradingForm = ref({
  grade: 0,
  comment: ''
});

const submitGrading = () => {
  console.log('提交评分:', gradingForm.value);
  // Implement submission logic here
};

const previousStudent = () => {
  if (currentStudentIndex.value > 0) {
    currentStudentIndex.value--;
  }
};

const nextStudent = () => {
  if (currentStudentIndex.value < tableData.value.length - 1) {
    currentStudentIndex.value++;
  }
};
</script>

<style scoped>
.grading-container {
  padding: 20px;
}

.custom-table {
  padding: 20px;
}

.button-col {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.highlight {
  background-color: orange;
}

.header {
  margin-bottom: 20px;
}

.content {
  margin-bottom: 20px;
}

.footer {
  display: flex;
  justify-content: space-between;
}

.right-align {
  text-align: right;
}
</style>
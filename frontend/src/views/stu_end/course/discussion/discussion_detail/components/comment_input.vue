<template>
  <div class="comment-input">
    <el-input
      type="textarea"
      v-model="content"
      placeholder="写下你的评论..."
      rows="3"
      class="comment-textarea"
    />
    <div class="button-container">
      <el-button
        type="primary"
        @click="submitComment"
        :disabled="!content"
        class="submit-button"
      >
        发布评论
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { getReplies, getget_thread } from "@/api/discussion";
import { useRoute } from "vue-router";
const route = useRoute();
console.log(route.params);
const threadId = route.params.id;

const props = defineProps({
  isFlag: {
    type: Boolean,
    default: false,
  },
});

watch(
  () => props.isFlag,
  () => {
    if (props.isFlag) {
      content.value = "";
    }
  }
);

const emits = defineEmits(["submitComment"]);

const content = ref("");

const submitComment = () => {
  // var date = new Date();
  // getReplies({
  //   content: content.value,
  //   threadId,
  //   replyId: 1,
  //   userId: localStorage.getItem('userId'),
  //   // TODO: Replace with current date
  //   createdAt: "" + date.getFullYear() + "-" + date.getMonth() + "-" + date.getDay() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() // "2024-06-24 21:35:47"
  // })
  emits("submitComment", content.value);
  // content.value = '';
};
function getget_threadList() {
  getget_thread({
    id: threadId,
  }).then((res) => {
    // console.log(res);
  });
}
getget_threadList();
</script>

<style scoped>
.comment-input {
  margin-bottom: 20px;
}

.comment-textarea {
  margin-bottom: 10px;
  /* 控制输入框与按钮之间的距离 */
}

.button-container {
  text-align: right;
  /* 将按钮右对齐 */
}

.submit-button {
  /* 如需调整按钮样式，可在此处添加 */
}
</style>

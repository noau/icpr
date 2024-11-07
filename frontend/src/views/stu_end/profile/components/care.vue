<template>
  <el-dialog
    :modelValue="dialogVisible"
    title="关注"
    width="800"
    @close="emits('cancel')"
  >
    <div class="flex itemBlock" v-for="i in list">
      <div class="flex flex-align-center">
        <el-avatar
          :size="60"
          style="margin-right: 20px"
          :src="
            i?.avatar
              ? i?.avatar
              : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          "
        />
        <div>
          <h2>姓名:{{ i?.subscriptionName }}</h2>
          <label class="mg10">学号:{{ i?.followingId }}</label>
        </div>
      </div>
      <div>
        <el-button type="primary" plain @click="cancel(i)">取消关注</el-button>
      </div>
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, defineProps, reactive, watch } from "vue";
import { userSubscriptions, getDeleteSubscription } from "@/api/user.js";
import { AsyncIterator } from "@kangc/v-md-editor";
import { ElMessage } from "element-plus";

const props = defineProps({
  dialogVisible: {
    type: Boolean,
  },
});

const emits = defineEmits(["cancel"]);

const userId = localStorage.getItem("userId");
const list = ref();
const getCare = async () => {
  const res = (await userSubscriptions({ id: userId })) as any;
  list.value = res?.userSubscriptions;
};

//取消
const cancel = async (item) => {
  const res = await getDeleteSubscription({
    subscriptionId: item.subscriptionId,
    followingId: userId,
    // subscriptionId: item.subscriptionId,
  });
  ElMessage("取消成功！");
  // getdDiscussionlike
};

watch(
  () => props.dialogVisible,
  (newVal) => {
    if (newVal) {
      getCare();
    }
  }
);
</script>
<style scoped>
.itemBlock {
  padding: 10px 0;
  width: 100%;
  border-bottom: 1px solid #ddd;
}
</style>

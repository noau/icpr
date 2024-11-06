<template>
    <el-dialog
      :modelValue="dialogVisible"
      title="粉丝"
      width="800"
      @close="emits('cancel')"
    >
      <div
        class="flex flex-align-center itemBlock"
        style="justify-content: flex-start"
        v-for="i in list"
      >
        <el-avatar
          :size="60"
          style="margin-right: 20px"
          :src="
            i?.avatar ||
            'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
          "
        />
        <div>
          <h2>姓名:{{ i?.followingName }}</h2>
          <label class="mg10">学号: {{ i?.followingId }}</label>
        </div>
      </div>
    </el-dialog>
  </template>
  
  <script lang="ts" setup>
  import { ref, defineProps, reactive, watch } from "vue";
  import { userFollowers } from "@/api/user.js";
  
  const props = defineProps({
    dialogVisible: {
      type: Boolean,
    },
  });
  const emits = defineEmits(["cancel"]);
  const userId = localStorage.getItem("userId");
  const list = ref();
  const getFans = async () => {
    const res = (await userFollowers({ id: userId })) as any;
    list.value = res?.userFollowers;
  };
  
  watch(
    () => props.dialogVisible,
    (newVal) => {
      if (newVal) {
        getFans();
      }
    }
  );
  </script>
  <style scoped>
  .itemBlock {
    padding: 10px 0;
    border-bottom: 1px solid #ddd;
  }
  </style>
  
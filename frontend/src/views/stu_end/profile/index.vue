<template>
  <div class="wrapper">
    <el-container>
      <!-- Header 占满整个页面宽度 -->
      <el-header>
        <stu_course_header />
      </el-header>

      <!-- Main 布局 -->
      <el-main>
        <div class="perInfo">
          <div>
            <div class="perInfoTop flex flex-align-center">
              <div class="flex flex-align-center">
                <div class="photos">
                  <el-avatar
                    :size="60"
                    style="margin-right: 20px"
                    :src="
                      userInfo?.avatar
                        ? userInfo?.avatar
                        : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
                    "
                  />
                  <label class="uploadImg" @click="photoVisible = true"
                    >上传头像</label
                  >
                </div>
                <div>
                  <h2>姓名:{{ userInfo?.name }}</h2>
                  <div>
                    <label class="mg10">学号:{{ userInfo?.id || "-" }}</label>
                    <label class="mg10"
                      >学院:{{ userInfo?.academy || "-" }}</label
                    >
                    <label class="mg10"
                      >班级:{{ userInfo?.userClass || "-" }}</label
                    >
                  </div>
                </div>
              </div>
              <div style="margin-top: 20px" class="flex">
                <div class="flex perInfoTop2-fr flex-align-center">
                  <div class="itemBlock" @click="fansVisible = true">
                    <label>粉丝</label>
                    <p>{{ userInfo?.fansNumber || 0 }}</p>
                  </div>
                  <div class="itemBlock" @click="careVisible = true">
                    <label>关注</label>
                    <p>{{ userInfo?.subscriptionsNumber || 0 }}</p>
                  </div>
                  <div class="itemBlock">
                    <label>发帖数</label>
                    <p>{{ userInfo?.threadNumber || 0 }}</p>
                  </div>
                </div>
                <!-- <el-button v-if="isSelf == 0" @click="subscription">关注</el-button> -->
                <!-- <el-button v-else @click="pwdVisible = true">更换密码</el-button> -->
                <el-button @click="pwdVisible = true">更换密码</el-button>
              </div>
            </div>
            <div class="perInfoTop2 flex flex">
              <div class="fl tabTop">
                <label
                  v-for="(item, index) in tab"
                  :key="index"
                  :class="active == index ? 'cur' : ''"
                  @click="tabList(item, index)"
                  >{{ item }}</label
                >
              </div>
            </div>

            <div class="bottomCon">
              <div class="blockBt">
                <div class="title">收藏夹</div>
                <div>
                  <!-- collectList -->
                  <div v-for="item in collectList" class="blocktag">
                    <el-button @click="getFList(item)" type="warning">{{
                      item.name
                    }}</el-button>
                    <el-icon
                      @click="deletFloder(item)"
                      style="margin-right: 10px"
                    >
                      <delete />
                    </el-icon>
                    <el-icon>
                      <EditPen class="editPen" @click="editFile(item)" />
                    </el-icon>
                  </div>
                  <span class="blocktag">
                    <el-button @click="fileVisible = true">
                      <el-icon> <Plus /> </el-icon>添加收藏夹
                    </el-button>
                  </span>
                </div>
              </div>

              <div class="blockBt">
                <div class="title">我的帖子</div>
                <div class="lineItem" v-for="i in userInfo?.threadList" :key="i.id" @click="openThread(i)">
                  <div class="til1">{{ i?.title }}</div>
                  <div class="til2">{{ i?.userName }}</div>
                  <div class="flex">
                    <div class="fl_til3 flex flex-align-center">
                      <span class=" flex flex-align-center">
                        <Star :theme="i.favorite ? 'filled' : 'outline'" style="color: #333;margin-right: 1px; margin-bottom: -2px;" />
                        {{ i?.favorites }}
                      </span>
                      <span class=" flex flex-align-center">
                        <Like
                          :theme="i.liked ? 'filled' : 'outline'"
                          size="15"
                          fill="#333"
                          style="margin-right: 0px; margin-bottom: -4px;"
                        />
                        {{ i.likes }}
                      </span>
                      <span>最近更新：{{ i?.updatedAt }}</span>
                    </div>
                    <div></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
    <changePwd :dialogVisible="pwdVisible" @cancel="pwdVisible = false" />
    <fans :dialogVisible="fansVisible" @cancel="fansVisible = false" />
    <care :dialogVisible="careVisible" @cancel="careVisible = false" />
    <uploadPhoto :dialogVisible="photoVisible" />
    <addFile
      :dialogVisible="fileVisible"
      @cancel="
        fileVisible = false;
        getUserFolders();
      "
    />
    <addFileEdit
      :folder="folder"
      :dialogVisible="fileEditVisible"
      @cancel="fileEditVisible = false"
    />
    <!-- 收藏的帖子抽屉 -->
    <el-drawer title="我是标题" v-model="drawer" :with-header="false">
      <div>
        <el-card
          shadow="always"
          :body-style="{ padding: '20px' }"
          v-for="(item, index) in collectMy"
          :key="index"
          style="margin-bottom: 10px; cursor: pointer;"
          @click="toThread(item)"
        >
          <template v-slot:header>
            <div  style="display: flex; flex-direction: column">
              <span style="font-weight: bold">{{ item.title }}</span>
              <span>{{ item.createdAt }}</span>
            </div>
          </template>
          <el-icon @click.stop="deletTie(item)" style="float: right">
            <delete />
          </el-icon>
        </el-card>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref } from "vue";
import changePwd from "./components/changePwd.vue";
import fans from "./components/fans.vue";
import care from "./components/care.vue";
import uploadPhoto from "./components/uploadPhoto.vue";
import addFile from "./components/addFile.vue";
import addFileEdit from "./components/addFileEdit.vue";
import { Like, Star } from "@icon-park/vue-next";
import {useRoute, useRouter} from 'vue-router';

import {
  userFolders,
  userUserInfo,
  userFavorites,
  userDelete_favorite,
  userDelete_folder,
  makeSubscription
} from "@/api/user.js";
import { ElMessage } from 'element-plus'

const route = useRoute();
const router = useRouter();

const pwdVisible = ref(false);
const fansVisible = ref(false);
const careVisible = ref(false);
const photoVisible = ref(false);
const fileVisible = ref(false);
const fileEditVisible = ref(false);
const drawer = ref(false);
const folder = ref();

const openThread = (post) => {
  router.push(`/stu-end/course/discussion/post/${post.id}`);
};

// 此主页的用户id
const userId = localStorage.getItem("userId");
// 是否是自己的id 1是自己的id 0不是自己的id
// const isSelf = +route.query.isSelf || 1;

const userInfo = ref();
const getInfo = async () => {
  const res = await userUserInfo({ id: userId });
  userInfo.value = res;
};
getInfo();
function deletFloder(item) {
  console.log(item.id);

  userDelete_folder({ id: item.id })
    .then((res) => {
      console.log(res);
      getUserFolders();
    })
    .catch((err) => {
      getUserFolders();
    });
}
function editFile(item) {
  // console.log(item);
  folder.value = item;
  localStorage.setItem("folder", JSON.stringify(item));

  fileEditVisible.value = true;
}

const toThread = (item) => {
  router.push(`/stu-end/course/discussion/post/${item.threadId}`);
}
// 获取收藏夹
const collectList = ref();
const getUserFolders = async () => {
  const res = await userFolders({ id: userId });
  collectList.value = res.folders;
};
setTimeout(() => {
  getUserFolders();
}, 300);

// 收藏的帖子
const collectMy = ref();

// 收藏夹功能
const getuserFavorites = async (id) => {
  const res = await userFavorites({ id });
  console.log(res, "resres");

  collectMy.value = res?.favorites;
};

function getFList(item) {
  drawer.value = true;
  getuserFavorites(item.id);
}

// 删除收藏的帖子
async function deletTie(item) {
  await userDelete_favorite({ id: item.id })
  getuserFavorites(item.folderId);
  ElMessage('删除成功')
}

// 关注他人
async function subscription() {
  //准备请求数据
  const data = {
    followingId: localStorage.getItem("userId"), //用户id
    subscriptionId: userId, // 被关注的用户id
  };
  await makeSubscription(data);
  ElMessage('关注成功')
}
</script>

<style scoped lang="scss">
.wrapper {
  width: 100%;
  position: absolute;
  width: 100%;
  left: 0;
  top: 0;
  min-height: 100vh;
  background: #f6f6f6;
}

.perInfo {
  width: 1280px;
  margin: 0 auto;

  .perInfoTop {
    background: url("@/assets/img/personBg.jpg") center no-repeat;
    height: 200px;
    color: #fff;
    padding: 120px 20px 0 20px;

    .photos {
      position: relative;

      .uploadImg {
        position: absolute;
        width: 60px;
        height: 60px;
        line-height: 60px;
        text-align: center;
        color: #fff;
        left: 0px;
        top: 0px;
        display: none;
        border-radius: 100%;
        background: rgb(0, 0, 0, 0.6);
      }
    }

    .photos:hover {
      .uploadImg {
        display: block;
      }
    }
  }

  .perInfoTop2-fr {
    padding: 0 20px;

    .itemBlock {
      font-size: 12px;
      margin-left: 20px;
      text-align: center;
      cursor: pointer;

      label {
        color: #222;
      }
    }
  }
}

.bottomCon {
  margin: 20px auto;

  .blockBt {
    background: #fff;
    border-radius: 10px;
    padding: 10px;
    margin: 20px 0;

    .title {
      font-weight: bold;
      margin: 10px 10px 20px 10px;
      padding-bottom: 15px;
      border-bottom: 1px solid #eee;
    }

    .blocktag {
      margin: 0 20px 10px 0;
      display: inline-block;

      .editPen {
        display: none;
      }
    }

    .blocktag:hover .editPen {
      display: block;
    }
  }

  .lineItem {
    padding: 10px 0px;
    margin: 0 10px;
    border-bottom: 1px solid #eee;
    cursor: pointer;

    .til1 {
      font-size: 16px;
      color: #000;
    }

    .til2 {
      color: #333;
      font-size: 14px;
      padding: 4px 0;
    }

    .fl_til3 {
      color: #999;
      font-size: 12px;

      span {
        margin-right: 10px;
      }
    }
  }
}

.avatar-uploader .el-upload {
  width: 68px;
  height: 68px;
}

.avatar-uploader .avatar {
  width: 68px;
  height: 68px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>

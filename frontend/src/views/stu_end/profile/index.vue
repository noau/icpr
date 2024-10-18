<template>
  <div class="wrapper">
    <el-container>
      <!-- Header 占满整个页面宽度 -->
      <el-header>
        <course_header/>
      </el-header>

      <!-- Main 布局 -->
      <el-main>
        <div class="perInfo">
          <div>
            <div class="perInfoTop flex  flex-align-center">
              <div class="flex  flex-align-center">
                <div class="photos">
                  <el-avatar :size="60" style="margin-right: 20px;"
                             :src="userInfo?.avatar?userInfo?.avatar:'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"
                  />
                  <label class="uploadImg" @click="photoVisible=true">上传头像</label>
                </div>
                <div>
                  <h2>姓名:{{ userInfo?.name }}</h2>
                  <div>
                    <label class="mg10">学号:{{ userInfo?.id || '-' }}</label>
                    <label class="mg10">学院:{{ userInfo?.academy || '-' }}</label>
                    <label class="mg10">班级:{{ userInfo?.userClass || '-' }}</label>
                  </div>
                </div>
              </div>
              <div style="margin-top:20px">
                <el-button @click='pwdVisible=true'>更换密码</el-button>
              </div>
            </div>
            <div class="perInfoTop2 flex flex">
              <div class="fl tabTop">
                <label v-for="(item,index) in tab" :key="index" :class="active==index?'cur':''"
                       @click="tabList(item,index)">{{ item }}</label>
              </div>
              <div class="flex perInfoTop2-fr flex-align-center">
                <div class="itemBlock" @click="fansVisible=true">
                  <label>粉丝</label>
                  <p>{{ userInfo?.fansNumber || 0 }}</p>
                </div>
                <div class="itemBlock" @click="careVisible=true">
                  <label>关注</label>
                  <p>{{ userInfo?.subscriptionsNumber || 0 }}</p>
                </div>
                <div class="itemBlock">
                  <label>发帖数</label>
                  <p>{{ userInfo?.threadNumber || 0 }}</p>
                </div>
              </div>
            </div>

            <div class="bottomCon">
              <div class="blockBt">
                <div class="title">收藏夹</div>
                <div>
                  <!-- collectList -->
                  <span v-for="i in 10" class="blocktag">
                      <el-button>名称{{ i?.name }}</el-button><el-icon><EditPen class="editPen"
                                                                                @click="fileVisible=true"/></el-icon>
                    </span>
                  <span class="blocktag"> <el-button @click="fileVisible=true"><el-icon><Plus/></el-icon>添加收藏夹</el-button></span>
                </div>
              </div>
              <div class="blockBt">
                <div class="title">我的收藏</div>
                <div class="lineItem" v-for="i in 5">
                  <div class="til1">我的收藏</div>
                  <div class="til2">我的收藏我的收藏我的收藏我的收藏</div>
                  <div class="flex">
                    <div class="fl_til3 flex flex-align-center">
                      <span><el-icon><Star/></el-icon>4444</span>
                      <span>最近更新：2024 10-1</span>
                    </div>
                    <div>
                      <el-dropdown>
                        <el-button type="primary">
                          编辑分组
                          <el-icon class="el-icon--right">
                            <arrow-down/>
                          </el-icon>
                        </el-button>
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item>Action 1</el-dropdown-item>
                            <el-dropdown-item>Action 2</el-dropdown-item>
                            <el-dropdown-item>Action 3</el-dropdown-item>
                            <el-dropdown-item disabled>Action 4</el-dropdown-item>
                            <el-dropdown-item divided>Action 5</el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </div>
                </div>
              </div>

              <div class="blockBt">
                <div class="title">我的帖子</div>
                <div class="lineItem" v-for="i in collectFlist">
                  <div class="til1">{{ i?.name }}</div>
                  <div class="til2">我的收藏我的收藏我的收藏我的收藏{{ i?.name }}</div>
                  <div class="flex">
                    <div class="fl_til3 flex flex-align-center">
                      <span><el-icon><Star/></el-icon>4444{{ i?.name }}</span>
                      <span>最近更新：2024 10-1{{ i?.name }}</span>
                    </div>
                    <div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </div>
      </el-main>
    </el-container>
    <changePwd :dialogVisible='pwdVisible' @cancel="pwdVisible=false"/>
    <fans :dialogVisible='fansVisible'/>
    <care :dialogVisible='careVisible'/>
    <uploadPhoto :dialogVisible='photoVisible'/>
    <addFile :dialogVisible='fileVisible' @cancel="fileVisible=false;getUserFolders()"/>
  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import changePwd from './components/changePwd.vue'
import fans from './components/fans.vue'
import care from './components/care.vue'
import uploadPhoto from './components/uploadPhoto.vue'
import addFile from './components/addFile.vue'
import {userFolders, userUserInfo, userCreate_favorite} from '@/api/user.js'
import {useUserStore} from '@/stores/user.js'

const tab = ref(['收藏夹', '我的收藏', '我的帖子']);
const active = ref(0);
const pwdVisible = ref(false);
const fansVisible = ref(false);
const careVisible = ref(false);
const photoVisible = ref(false);
const fileVisible = ref(false);

const tabList = (item, index) => {
  active.value = index;
}

//个人信息
const userId = useUserStore()?.id || localStorage.getItem('userId');
const token = useUserStore()?.token;
console.log(`UserId: ${userId}`)
console.log(`Token: ${token}`)
const userInfo = ref();
const getInfo = async () => {
  console.log("Fetching user info")
  const res = await userUserInfo({id: userId, token})
  useUserStore().setId(userInfo.value.id)
  console.log(`User Info: ${res}`)
  userInfo.value = res
}

getInfo();

// 获取收藏夹
const collectList = ref();
const getUserFolders = async () => {
  const res = await userFolders({id: userId, token})
  collectList.value = res?.folders;
}
getUserFolders();

//收藏的帖子
const collectFlist = ref();
const getuserCreateFavorite = async () => {
  const res = await userCreate_favorite({token})
  collectFlist.value = res?.folders;
}
getuserCreateFavorite();
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
    background: url('@/assets/img/personBg.png') center no-repeat;
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

  .perInfoTop2 {
    background: #fff;
    height: 50px;

    .tabTop {
      line-height: 50px;

      label {
        padding: 0 10px;
        display: inline-block;
        cursor: pointer;
      }

      label.cur {
        color: #07395f;
        font-weight: bold;
        border-bottom: 2px solid #07395f;
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
        color: #99a2aa;
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
        display: none
      }
    }

    .blocktag:hover .editPen {
      display: block
    }
  }

  .lineItem {
    padding: 10px 0px;
    margin: 0 10px;
    border-bottom: 1px solid #eee;

    .til1 {
      font-size: 16px;
      // font-weight: bold;
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
 
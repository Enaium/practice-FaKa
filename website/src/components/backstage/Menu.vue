<script setup lang="ts">
import {useRoute} from "vue-router";
import {useUserStatus} from "@/store";
import {ref} from "vue";
import {getUser, post} from "@/util/request";
import {Handbag, HomeFilled, Shop, Wallet} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import Logo from "@/assets/vue.svg"

const route = useRoute()
const userStatus = useUserStatus()

const handleSelect = (key: string, keyPath: string[]) => {
    window.$router.push({name: key})
}

const store = ref(false)

getUser(userStatus.id).then(r => {
    if (r.metadata.store) {
        store.value = true
    }
})

const logout = () => {
    ElMessageBox.confirm(
        '你确定要登出吗?',
        '警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            post("/merchant/auth/logout").then(r => {
                if (r.code === 200) {
                    ElMessage({message: "登出成功", type: "success"})
                    window.$router.push({name: 'welcome'})
                }
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消登出',
            })
        })
}
</script>

<template>
  <el-menu
          class="flex-column justify-between"
          :default-active="route.name"
          @select="handleSelect">

    <div class="flex-column">
      <div class="flex-column justify-center align-center">
        <el-image :src="Logo" style="width: 100px;"/>
        <div>发卡</div>
      </div>
      <el-sub-menu index="merchant">
        <template #title>
          <el-icon style="color: #409EFC">
            <HomeFilled/>
          </el-icon>
          <span>我的账户</span>
        </template>
        <el-menu-item index="summarize">
          信息总汇
        </el-menu-item>
        <el-menu-item index="user-info">
          商户信息
        </el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="commodity">
        <template #title>
          <el-icon style="color: #409EFC">
            <Handbag/>
          </el-icon>
          <span>商品管理</span>
        </template>
        <el-menu-item index="add-category">
          添加分类
        </el-menu-item>
        <el-menu-item index="categories">
          商品分类
        </el-menu-item>
        <el-menu-item index="add-goods">
          添加商品
        </el-menu-item>
        <el-menu-item index="goods">
          商品列表
        </el-menu-item>
        <el-menu-item index="add-kami">
          添加卡密
        </el-menu-item>
        <el-menu-item index="kamis">
          卡密列表
        </el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="store">
        <template #title>
          <el-icon style="color: #409EFC">
            <Shop/>
          </el-icon>
          <span>店铺管理</span>
        </template>
        <el-menu-item index="store-info" v-if="store">
          店铺信息
        </el-menu-item>
        <el-menu-item index="create-store" v-else>
          创建店铺
        </el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="data">
        <template #title>
          <el-icon style="color: #409EFC">
            <Wallet/>
          </el-icon>
          <span>交易数据</span>
        </template>
        <el-menu-item index="orders">
          订单列表
        </el-menu-item>
      </el-sub-menu>
    </div>
    <el-button type="danger" @click="logout">登出</el-button>
  </el-menu>
</template>

<style scoped>
* {
    font-weight: 700;
}
</style>

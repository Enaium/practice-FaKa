<script setup lang="ts">
import {useRoute} from "vue-router";
import {ref} from "vue";
import {isLogin} from "@/util/request";

const route = useRoute();

const handleSelect = (key: string, keyPath: string[]) => {
    window.$router.push({name: key})
}

const loginOrRegister = () => {
    window.$router.push({name: "login"})
}

const login = ref(false);

isLogin().then(r => {
    login.value = r.metadata
})

</script>

<template>
  <el-menu
          :default-active="route.name"
          mode="horizontal"
          @select="handleSelect"
          :ellipsis="false"
          style="justify-content:space-around;align-items: center"
  >
    <div class="flex">
      <el-menu-item index="welcome">欢迎</el-menu-item>
      <el-menu-item index="feature">功能</el-menu-item>
      <el-menu-item index="contact">联系</el-menu-item>
    </div>
    <el-button @click="loginOrRegister" v-if="!login">登陆/注册</el-button>
    <el-button @click="this.$router.push({name:'summarize'})" type="primary" v-else>进入后台</el-button>
  </el-menu>
</template>

<style scoped>

</style>

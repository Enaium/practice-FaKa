<script setup lang="ts">


import {reactive, ref} from 'vue'
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {useUserStatus} from "@/store";
import {post} from "@/util/request";
import {useRoute} from "vue-router";

const route = useRoute();
const userStatus = useUserStatus()

const loginForm = reactive({
    username: undefined,
    password: undefined
})

const loginRef = ref<FormInstance>()

const loginRule = reactive<FormRules>({
    username: [
        {required: true, message: '请输入用户名', trigger: 'blur'},
    ],
    password: [
        {required: true, message: '请输入密码', trigger: 'blur'},
    ]
})

const login = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            post<any>("/merchant/auth/login", loginForm).then(r => {
                if (r.code === 200) {
                    userStatus.setToken(r.metadata.token)
                    userStatus.setId(r.metadata.id)
                    ElMessage({message: "登录成功", type: "success"})
                    window.$router.push({name: "welcome"})
                }
            })
        } else {
            return false
        }
    })
}
</script>

<template>
  <div style="display: flex;justify-content: center;align-items: center;height: 100vh">
    <el-card style="width: 500px">
      <template #header>
        <div>登录</div>
      </template>
      <el-form
              label-position="right"
              label-width="100px"
              :model="loginForm"
              ref="loginRef"
              :rules="loginRule"
      >
        <el-form-item prop="username" label="账号">
          <el-input v-model="loginForm.username"/>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input v-model="loginForm.password"/>
        </el-form-item>
        <div style="display: flex;flex-direction: row-reverse">
          <el-button link type="primary" @click="this.$router.push({name:'register'})">还没有账号? 快去注册</el-button>
        </div>
        <el-form-item>
          <el-button type="primary" style="width: 100%;" @click="login(loginRef)">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>

</style>

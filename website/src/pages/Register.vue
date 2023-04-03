<script setup lang="ts">


import {reactive, ref} from 'vue'
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {post} from "@/util/request";
import {useRouter} from "vue-router";

const router = useRouter();

const registerForm = reactive({
    username: undefined,
    password: undefined,
    reenter: undefined
})

const registerRef = ref<FormInstance>()

const reenterPassword = (rule: any, value: any, callback: any) => {
    if (!value) {
        callback(new Error('请输入密码'))
    } else {
        if (registerForm.password !== registerForm.reenter) {
            callback(new Error('请输入相同的密码'))
        }
        callback()
    }
}

const loginRules = reactive<FormRules>({
    username: [
        {required: true, message: '请输入用户名', trigger: 'blur'},
    ],
    password: [
        {required: true, message: '请输入密码', trigger: 'blur'},
    ],
    reenter: [
        {required: true, validator: reenterPassword, trigger: 'blur'},
    ]
})

const register = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            post("/merchant/auth/register", registerForm).then(r => {
                if (r.code === 200) {
                    ElMessage({message: "注册成功", type: "success"})
                    router.push({name: "login"})
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
        <div>注册</div>
      </template>
      <el-form
              label-position="right"
              label-width="100px"
              :model="registerForm"
              ref="registerRef"
              :rules="loginRules"
      >
        <el-form-item prop="username" label="账号">
          <el-input v-model="registerForm.username"/>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input v-model="registerForm.password"/>
        </el-form-item>
        <el-form-item prop="reenter" label="重复密码">
          <el-input v-model="registerForm.reenter"/>
        </el-form-item>
        <div style="display: flex;flex-direction: row-reverse">
          <el-button link type="primary" @click="this.$router.push({name:'login'})">已有账号? 快去登陆</el-button>
        </div>
        <el-form-item>
          <el-button type="primary" style="width: 100%;" @click="register(registerRef)">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>

</style>

<!--
  - Copyright 2023 Enaium
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<script setup lang="ts">
import {reactive, ref} from "vue";
import {IStore} from "@/util/model";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {post} from "@/util/request";
import {buildParameter} from "@/util";
import {useUserStatus} from "@/store";

const useStatus = useUserStatus()

const form = reactive<IStore>({closing: false})
const formRef = ref<FormInstance>()
const rule = reactive<FormRules>({
    name: [
        {required: true, message: "请输入店铺名称", trigger: "blur"},
        {min: 2, max: 20, message: "长度为2-20"}
    ],
    path: [
        {required: true, message: "请输入店铺地址", trigger: "blur"},
        {min: 6, max: 10, message: "长度为6-10"},
        {pattern: /^[a-z0-9A-Z-_.~]+$/, message: "只能包含数字和大小写字母,特殊字符只能包含-_.~"}
    ],
    closing: [{required: true, message: "请输选择是否打烊", trigger: "blur"}]
})

const submit = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            post(`/merchant/store${buildParameter({
                user: useStatus.id
            })}`, form).then(r => {
                if (r.code == 200) {
                    ElMessage({message: "创建成功", type: "success"})
                }
            })
        } else {
            return false
        }
    })
}
</script>

<template>
  <div>创建店铺</div>
  <el-divider/>
  <el-form label-width="100" :model="form" ref="formRef" :rules="rule">
    <el-form-item prop="name" label="店铺名称">
      <el-input v-model="form.name"/>
    </el-form-item>
    <el-form-item prop="path" label="店铺地址">
      <el-input v-model="form.path"/>
    </el-form-item>
    <el-form-item prop="closing" label="打烊">
      <el-switch v-model="form.closing"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit(formRef)">
        创建
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>

</style>

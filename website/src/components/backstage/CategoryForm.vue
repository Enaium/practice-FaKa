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
import {useUserStatus} from "@/store";
import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {ICategory} from "@/util/model";
import {patch, post} from "@/util/request";

const props = defineProps<{
    form: ICategory
}>()

const userStatus = useUserStatus();

const categoryRef = ref<FormInstance>()

const rule = reactive<FormRules>({
    name: [
        {required: true, message: "请填写分类名称", trigger: "blur"},
        {min: 2, max: 40, message: "长度需在2-40位之间"}
    ],
    priority: [
        {required: true, message: "请填写优先级", trigger: "blur"}
    ]
})

const submit = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            if (props.form.id) {
                patch(`/commodity/category/${props.form.id}`, props.form).then(r => {
                    if (r.code === 200) {
                        ElMessage({message: "修改成功", type: "success"})
                    }
                })
            } else {
                post(`/commodity/category?user=${userStatus.getId}`, props.form).then(r => {
                    if (r.code === 200) {
                        ElMessage({message: "添加成功", type: "success"})
                    }
                })
            }
        } else {
            return false
        }
    })
}
</script>

<template>
  <el-form :model="props.form" label-width="100" ref="categoryRef" :rules="rule" v-if="props.form">
    <el-form-item prop="name" label="分类名称">
      <el-input v-model="props.form.name"/>
    </el-form-item>
    <el-form-item prop="priority" label="优先级">
      <el-input-number v-model="props.form.priority"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit(categoryRef)">
        提交
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>

</style>

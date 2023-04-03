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
import {FormInstance} from "element-plus";

const props = defineProps<{
    goods: number
}>()

const form = reactive({
    kaimi: ""
})
const formRef = ref<FormInstance>()
const submit = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {

        } else {
            return false
        }
    })
}
</script>

<template>
  <el-form :model="form" ref="formRef">
    <el-form-item prop="kami" label="卡密">
      <el-input v-model="form.kaimi" type="textarea"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit">添加</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>

</style>

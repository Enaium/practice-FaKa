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
import {get, post} from "@/util/request";
import {ICategory, IKami, IPage} from "@/util/model";
import {reactive, ref} from "vue";
import {useUserStatus} from "@/store";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {buildParameter} from "@/util";

const userStatus = useUserStatus()

const goodsOptions = ref<{
    value: number
    label: string
}[]>([])
const categoryLoading = ref(false)

const form = reactive<IKami>({})
const formRef = ref<FormInstance>()
const searchGoods = (value: string) => {
    if (value) {
        categoryLoading.value = true
        get<IPage<ICategory>>(`/commodity/goods?keyword=${value}&user=${userStatus.id}`).then(r => {
            goodsOptions.value = r.metadata.content.map((item) => {
                return {value: item.id!, label: item.name!}
            })
            categoryLoading.value = false
        })
    } else {
        goodsOptions.value = []
    }
}

const submit = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            post(`/commodity/kami${buildParameter({
                goods: form.goodsId,
                user: userStatus.id,
            })}`, form).then(r => {
                if (r.code === 200) {
                    ElMessage({message: "添加成功", type: "success"})
                }
            })
        } else {
            return false
        }
    })
}

const rule = reactive<FormRules>({
    goodsId: [
        {required: true, message: "请选择商品", trigger: "blur"}
    ],
    content: [
        {required: true, message: "请输入卡密", trigger: "blur"}
    ]
})
</script>

<template>
  <div>添加卡密</div>
  <el-divider/>
  <el-form :model="form" label-width="100" :rules="rule" ref="formRef">
    <el-form-item prop="goodsId" label="商品名称">
      <el-select
              v-model="form.goodsId"
              filterable
              remote
              reserve-keyword
              placeholder="请输入关键字搜索分类"
              :remote-method="searchGoods"
              :loading="categoryLoading"
      >
        <el-option
                v-for="item in goodsOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item prop="content" label="卡密">
      <el-input v-model="form.content" type="textarea"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit(formRef)">
        添加
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>

</style>

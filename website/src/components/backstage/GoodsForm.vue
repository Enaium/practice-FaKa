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
import {GoodsType, ICategory, IGoods, IPage, SaleOrderType} from "@/util/model";
import {useUserStatus} from "@/store";
import {reactive, ref} from "vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import {get, patch, post} from "@/util/request";
import {goodsTypeParse, saleOrderTypeParse} from "@/util";

const props = defineProps<{
    form: IGoods
}>()

const userStatus = useUserStatus();

const categoryOptions = ref<{
    value: number
    label: string
}[]>([])
const categoryLoading = ref(false)

const goodsRef = ref<FormInstance>()


const rule = reactive<FormRules>({
    categoryId: [
        {required: true, message: "请选择商品分类", trigger: "blur"},
    ],
    name: [
        {required: true, message: "请输入商品名称", trigger: "blur"},
    ],
    saleOrder: [
        {required: true, message: "请选择出售顺序", trigger: "blur"},
    ],
    priority: [
        {required: true, message: "请输入优先级", trigger: "blur"}
    ],
    price: [
        {required: true, message: "请输入商品价格", trigger: "blur"},
        {pattern: /^[0-9|^\\.]/, message: "不能为负数"},
    ],
    type: [
        {required: true, message: "请选择卡密类型", trigger: "blur"}
    ],
    description: [
        {required: true, message: "请输入描述", trigger: "blur"},
        {min: 10, max: 500, message: "长度为10-500"}
    ],
})

const searchCategory = (value: string) => {
    if (value) {
        categoryLoading.value = true
        get<IPage<ICategory>>(`/commodity/category?keyword=${value}&user=${userStatus.id}`).then(r => {
            categoryOptions.value = r.metadata.content.map((item) => {
                return {value: item.id!, label: item.name!}
            })
            categoryLoading.value = false
        })
    } else {
        categoryOptions.value = []
    }
}

const submit = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            if (props.form.id) {
                patch(`/commodity/goods/${props.form.id}`, props.form).then(r => {
                    if (r.code == 200) {
                        ElMessage({message: "修改成功", type: "success"})
                    }
                })
            } else {
                post(`/commodity/goods?category=${props.form.categoryId}&user=${userStatus.id}`, props.form).then(r => {
                    if (r.code == 200) {
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
  <el-form label-width="100" :model="form" ref="goodsRef" :rules="rule" v-if="props.form">
    <el-form-item prop="categoryId" label="商品分类">
      <el-select
              v-model="form.categoryId"
              filterable
              remote
              reserve-keyword
              placeholder="请输入关键字搜索分类"
              :remote-method="searchCategory"
              :loading="categoryLoading"
      >
        <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
        />
      </el-select>
    </el-form-item>
    <el-form-item prop="name" label="商品名称">
      <el-input v-model="form.name"/>
    </el-form-item>
    <el-form-item prop="saleOrder" label="出售顺序">
      <el-select v-model="form.saleOrder">
        <el-option
                v-for="(item,index) in SaleOrderType"
                :key="index"
                :label="saleOrderTypeParse(item)"
                :value="item"
        />
      </el-select>
    </el-form-item>
    <el-form-item prop="priority" label="优先级">
      <el-input-number v-model="form.priority"/>
    </el-form-item>
    <el-form-item prop="price" label="商品价格">
      <el-input-number v-model="form.price" :step="0.1" :max="20000"/>
    </el-form-item>
    <el-form-item prop="type" label="卡密类型">
      <el-select v-model="form.type">
        <el-option
                v-for="(item,index) in GoodsType"
                :key="index"
                :label="goodsTypeParse(item)"
                :value="item"
        />
      </el-select>
    </el-form-item>
    <el-form-item prop="description" label="描述">
      <el-input v-model="form.description" type="textarea"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit(goodsRef)">
        提交
      </el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>

</style>

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
import {reactive, ref, watch} from "vue";
import {IGoods, IPage} from "@/util/model";
import {del, get} from "@/util/request";
import {buildParameter, goodsTypeParse, saleOrderTypeParse} from "@/util";
import dayjs from "dayjs";
import {Delete, Edit, Plus, Search} from "@element-plus/icons-vue";
import GoodsForm from "@/components/backstage/GoodsForm.vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {useRouter} from "vue-router";
import KamiForm from "@/components/backstage/KamiForm.vue";

const router = useRouter()

const userStatus = useUserStatus();

const goods = ref<IPage<IGoods>>()

const page = ref(1)

const keyword = ref()

const refresh = () => {
    get<IPage<IGoods>>(`/commodity/goods${buildParameter({
        keyword: keyword.value,
        page: page.value - 1,
        user: userStatus.id
    })}`).then(r => {
        goods.value = r.metadata
    })
}

refresh()

watch(page, () => {
    refresh()
})

const deleteGoods = (id: number) => {
    ElMessageBox.confirm(
        '你确定要删除这个分类吗?',
        '警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            del(`/commodity/goods/${id}`).then(r => {
                if (r.code == 200) {
                    ElMessage({
                        type: 'success',
                        message: '删除成功',
                    })
                }
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消删除',
            })
        })
}

const editVisible = ref(false)
const addKamiVisible = ref(false)
let currentForm = reactive<IGoods>({})
let currentGoods = -1;
</script>

<template>
  <div v-if="goods">
    <div class="flex align-center">
      <div class="flex align-center" style="gap: 10px">
        <div style="width: 120px">商品列表</div>
        <el-input v-model="keyword" placeholder="请输入商品/分类名称" :prefix-icon="Search"/>
        <el-button type="primary" @click="refresh">搜索</el-button>
      </div>
      <div style="flex-grow: 1"/>
      <el-button type="primary" @click="router.push({ name: 'add-goods' })">添加商品</el-button>
    </div>
    <el-divider/>
    <el-table :data="goods.content" border>
      <template #empty>
        <el-empty>
          <el-button type="primary" @click="router.push({name:'add-goods'})">添加商品</el-button>
        </el-empty>
      </template>
      <el-table-column prop="category" label="分类">
        <template #default="scope">
          {{ scope.row.category.name }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="priority" label="显示顺序(优先级)"/>
      <el-table-column prop="price" label="价格"/>
      <el-table-column prop="type" label="类型">
        <template #default="scope">
          {{ goodsTypeParse(scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column prop="saleOrder" label="发售顺序">
        <template #default="scope">
          {{ saleOrderTypeParse(scope.row.saleOrder) }}
        </template>
      </el-table-column>
      <el-table-column prop="stock" label="总库存">
        <template #default="scope">
          {{ scope.row.stock }}
        </template>
      </el-table-column>
      <el-table-column width="160" prop="createTime" label="创建时间">
        <template #default="scope">
          {{ dayjs(scope.row.createTime).format("YYYY-MM-DD HH:MM:ss") }}
        </template>
      </el-table-column>
      <el-table-column width="160" prop="updateTime" label="更新时间">
        <template #default="scope">
          {{ dayjs(scope.row.updateTime).format("YYYY-MM-DD HH:MM:ss") }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="165">
        <template #default="scope">
          <el-button-group class="ml-4">
            <el-button type="primary" :icon="Edit" @click="() => { editVisible = true; currentForm = scope.row; }"/>
            <el-button type="warning" :icon="Plus" @click="addKamiVisible = true"/>
            <el-button type="danger" :icon="Delete" @click="deleteGoods(scope.row.id)"/>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-center" style="margin-top: 1rem">
      <el-pagination background layout="prev, pager, next" :total="goods.totalElements" :page-size="goods.size"
                     v-model:current-page="page"/>
    </div>
  </div>

  <el-dialog v-model="editVisible" title="修改商品">
    <goods-form :form="currentForm"/>
  </el-dialog>

  <el-dialog v-model="addKamiVisible" title="添加卡密">
    <kami-form :goods="currentGoods"/>
  </el-dialog>
</template>

<style scoped></style>

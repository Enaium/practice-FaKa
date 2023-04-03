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
import {del, get} from "@/util/request";
import {reactive, ref, watch} from "vue";
import {ICategory, IPage} from "@/util/model";
import {useUserStatus} from "@/store";
import dayjs from "dayjs";
import {Delete, Edit, Search} from "@element-plus/icons-vue";
import {ElMessage, ElMessageBox} from "element-plus";
import CategoryForm from "@/components/backstage/CategoryForm.vue";
import {buildParameter} from "@/util";
import {useRouter} from "vue-router";

const router = useRouter()

const userStatus = useUserStatus();

const categories = ref<IPage<ICategory>>()

const page = ref(1)

const keyword = ref()

const refresh = () => {
    get<IPage<ICategory>>(`/commodity/category${buildParameter({
        keyword: keyword.value,
        page: page.value - 1,
        user: userStatus.id
    })}`).then(r => {
        categories.value = r.metadata
    })
}

refresh()

watch(page, () => {
    refresh()
})

const deleteCategory = (id: number) => {
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
            del(`/commodity/category/${id}`).then(r => {
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

const visible = ref(false)
let form = reactive<ICategory>({})
</script>

<template>
  <div v-if="categories">
    <div class="flex align-center">
      <div class="flex align-center" style="gap: 10px">
        <div style="width: 120px">商品分类</div>
        <el-input v-model="keyword" placeholder="请输入分类名称" :prefix-icon="Search"/>
        <el-button type="primary" @click="refresh">搜索</el-button>
      </div>
      <div style="flex-grow: 1"/>
      <el-button type="primary" @click="router.push({ name: 'add-category' })">添加分类</el-button>
    </div>

    <el-divider/>

    <el-table :data="categories.content" border>
      <template #empty>
        <el-empty>
          <el-button type="primary" @click="router.push({name:'add-category'})">添加分类</el-button>
        </el-empty>
      </template>
      <el-table-column prop="name" label="分类名称"/>
      <el-table-column prop="goods" label="商品数量">
        <template #default="scope">
          {{ scope.row.goods.length }}
        </template>
      </el-table-column>
      <el-table-column prop="priority" label="显示顺序(优先级)"/>
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
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button-group class="ml-4">
            <el-button type="primary" :icon="Edit" @click="() => { visible = true; form = scope.row }"/>
            <el-button type="danger" :icon="Delete" @click="deleteCategory(scope.row.id)"/>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>

    <div class="flex justify-center" style="margin-top: 1rem">
      <el-pagination background layout="prev, pager, next" :total="categories.totalElements"
                     :page-size="categories.size"
                     v-model:current-page="page"/>
    </div>
  </div>

  <el-dialog v-model="visible" title="修改分类">
    <category-form :form="form"/>
  </el-dialog>
</template>

<style scoped></style>

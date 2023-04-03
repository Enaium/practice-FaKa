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

import {useRouter} from "vue-router";
import {useUserStatus} from "@/store";
import {get} from "@/util/request";
import {IKami, IPage} from "@/util/model";
import {buildParameter, kamiStatusTypeParse} from "@/util";
import {ref, watch} from "vue";
import dayjs from "dayjs";
import {Search} from "@element-plus/icons-vue";

const router = useRouter()

const userStatus = useUserStatus()

const kamis = ref<IPage<IKami>>()

const page = ref(1)
const keyword = ref()

const refresh = () => {
    get<IPage<IKami>>(`/commodity/kami${buildParameter({
        keyword: keyword.value,
        page: page.value - 1,
        user: userStatus.id
    })}`).then(r => {
        kamis.value = r.metadata
    })
}

refresh()

watch(page, () => {
    refresh()
})
</script>

<template>
  <div v-if="kamis">
    <div class="flex align-center">
      <div class="flex align-center" style="gap: 10px">
        <div style="width: 120px">卡密列表</div>
        <el-input v-model="keyword" placeholder="请输入商品/分类名称/卡密" :prefix-icon="Search"/>
        <el-button type="primary" @click="refresh">搜索</el-button>
      </div>
      <div style="flex-grow: 1"/>
      <el-button type="primary" @click="router.push({ name: 'add-kami' })">添加卡密</el-button>
    </div>
    <el-divider/>
    <el-table :data="kamis.content" border>
      <template #empty>
        <el-empty>
          <el-button type="primary" @click="router.push({name:'add-kami'})">添加卡密</el-button>
        </el-empty>
      </template>
      <el-table-column width="160" prop="createTime" label="创建时间">
        <template #default="scope">
          {{ dayjs(scope.row.createTime).format("YYYY-MM-DD HH:MM:ss") }}
        </template>
      </el-table-column>
      <el-table-column prop="name" label="商品名称">
        <template #default="scope">
          {{ scope.row.goods.name }}
        </template>
      </el-table-column>
      <el-table-column prop="content" label="卡密信息" width="53">
        <template #default="scope">
          <el-tooltip :content="scope.row.content">
            <el-text type="primary">查看</el-text>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column width="160" prop="updateTime" label="卖出时间">
        <template #default="scope">
          {{ dayjs(scope.row.updateTime).format("YYYY-MM-DD HH:MM:ss") }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="销售状态">
        <template #default="scope">
          {{ kamiStatusTypeParse(scope.row.status) }}
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-center" style="margin-top: 1rem">
      <el-pagination background layout="prev, pager, next" :total="kamis.totalElements" :page-size="kamis.size"
                     v-model:current-page="page"/>
    </div>
  </div>
</template>

<style scoped>

</style>

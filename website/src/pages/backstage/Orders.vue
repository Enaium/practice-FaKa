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
import {get} from "@/util/request";
import {useUserStatus} from "@/store";
import {IOrder, IPage, PayType} from "@/util/model";
import {ref, watch} from "vue";
import {buildParameter, orderStatusTypeParse} from "@/util";
import dayjs from "dayjs";

const userStatus = useUserStatus();

const orders = ref<IPage<IOrder>>()

const page = ref(1)

const refresh = () => {
    get<IPage<IOrder>>(`/merchant/user/${userStatus.id}/orders${buildParameter({
        page: page.value - 1
    })}`).then(r => {
        orders.value = r.metadata
    })
}

refresh()

watch(page, () => {
    refresh()
})

const income = ref(0)
const volume = ref(0)

get<number>(`/merchant/user/${userStatus.id}/orders/income`).then(r => {
    income.value = r.metadata
})

get<number>(`/merchant/user/${userStatus.id}/orders/volume`).then(r => {
    volume.value = r.metadata
})

</script>

<template>
  <div v-if="orders">
    <div>订单列表</div>
    <el-divider/>
    <div class="history">
      <el-card class="item">
        <div>总收益</div>
        <div>{{ income }}</div>
      </el-card>
      <el-divider direction="vertical"/>
      <el-card class="item">
        <div>总支付</div>
        <div>0</div>
      </el-card>
      <el-divider direction="vertical"/>
      <el-card class="item">
        <div>实际所得</div>
        <div>0</div>
      </el-card>
      <el-divider direction="vertical"/>
      <el-card class="item">
        <div>销售卡密</div>
        <div>{{ volume }}</div>
      </el-card>
    </div>
    <el-divider/>
    <el-table :data="orders.content" border>
      <template #empty>
        <el-empty>
          没有订单
        </el-empty>
      </template>
      <el-table-column prop="id" label="单号" width="53">
        <template #default="scope">
          <el-tooltip :content="scope.row.goods.id">
            <el-text type="primary">查看</el-text>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="goods" label="商品">
        <template #default="scope">
          {{ scope.row.goods.name }}
        </template>
      </el-table-column>
      <el-table-column prop="kamis" label="卡密数量">
        <template #default="scope">
          {{ scope.row.kamis.length }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          {{ orderStatusTypeParse(scope.row.status) }}
        </template>
      </el-table-column>
      <el-table-column prop="payType" label="支付方式">
        <template #default="scope">
          {{ scope.row.payType == PayType.ALIPAY ? "支付宝" : "微信" }}
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="总价">
        <template #default="scope">
          {{ scope.row.amount }}
        </template>
      </el-table-column>
      <el-table-column prop="quantity" label="数量">
        <template #default="scope">
          {{ scope.row.amount }}
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间">
        <template #default="scope">
          {{ dayjs(scope.row.createTime).format("YYYY-MM-DD HH:MM:ss") }}
        </template>
      </el-table-column>
      <el-table-column prop="updateTime" label="更新时间">
        <template #default="scope">
          {{ dayjs(scope.row.updateTime).format("YYYY-MM-DD HH:MM:ss") }}
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-center" style="margin-top: 1rem">
      <el-pagination background layout="prev, pager, next" :total="orders.totalElements" :page-size="orders.size"
                     v-model:current-page="page"/>
    </div>
  </div>
</template>

<style scoped>
.history {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.history .item {
    width: 200px;
    height: 100px;
    background: #409EFC;
    color: white;
    font-weight: 800;
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
}
</style>

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
import {useOrderHistory} from "@/store";
import {ElMessage, ElMessageBox} from "element-plus";

const orderHistory = useOrderHistory()

const clear = () => {
    ElMessageBox.confirm(
        '你确定要清除订单记录吗?',
        '警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            orderHistory.orders = []
            ElMessage({
                type: 'success',
                message: '清除成功',
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消清除',
            })
        })
}
</script>

<template>
  <el-button type="danger" style="margin-bottom: 1rem;width: 100%" @click="clear"
             v-if="orderHistory.orders.length">清除
  </el-button>
  <el-alert :closable="false" type="error" v-else>无订单记录</el-alert>
  <el-descriptions :column="1" border>
    <el-descriptions-item label="订单" v-for="order in orderHistory.getOrders">{{
        order
      }}
    </el-descriptions-item>
  </el-descriptions>
</template>

<style scoped>

</style>

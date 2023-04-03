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
import {useRoute, useRouter} from "vue-router";
import {IOrder, OrderStatusType} from "@/util/model";
import {get} from "@/util/request";
import {ref} from "vue";
import Kamis from "@/components/Kamis.vue";

const router = useRouter();
const route = useRoute();
const title = ref()
const order = ref<IOrder>()

if (route.query.out_trade_no) {
    get<IOrder>(`/pay/order/${route.query.out_trade_no}`).then(r => {
        if (r.code === 200) {
            switch (r.metadata.status) {
                case OrderStatusType.CLOSED:
                    title.value = "交易关闭"
                    break;
                case OrderStatusType.FINISHED:
                    title.value = "交易完成"
                    break;
                case OrderStatusType.SUCCESS:
                    title.value = "交易成功"
                    order.value = r.metadata
                    break;
                case OrderStatusType.WAIT_PAY:
                    title.value = "等待支付"
                    break;
                case OrderStatusType.WAIT_CREATE:
                    title.value = "交易未创建"
                    break;
            }
        }
    })
}
</script>

<template>
  <el-card style="margin: 5rem;display: flex;justify-content: center" v-if="order">
    <div>商品: {{ order.goods.name }}订单号:{{ order.id }} 单价:{{ order.goods.price }} 总价:{{ order.amount }}</div>
    <div style="text-align: center">{{ title }}</div>
    <kamis :data="order.kamis" v-if="order.kamis"/>
    <el-button type="primary" style="width: 100%"
               @click="router.push({name:'purchase',params:{path:order.user.store.path}})">返回
    </el-button>
    <el-alert type="warning" :closable="false" v-if="order.user?.info?.payTip">{{ order.user.info.payTip }}</el-alert>
  </el-card>
</template>

<style scoped>

</style>

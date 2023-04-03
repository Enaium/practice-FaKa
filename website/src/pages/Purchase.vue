<script setup lang="ts">

import {h, reactive, ref} from "vue";
import {ElMessage, ElMessageBox, FormInstance, FormRules} from "element-plus";
import {get, post} from "@/util/request";
import {GoodsType, ICategory, IGoods, IOrder, IPage, IStore, OrderStatusType, PayType} from "@/util/model";
import {buildParameter} from "@/util";
import {useOrderHistory} from "@/store";
import OrderHistory from "@/components/OrderHistory.vue";
import {Search} from "@element-plus/icons-vue";
import Kamis from "@/components/Kamis.vue";
import {useRouter} from "vue-router";

const props = defineProps<{
    path: string
}>()

const router = useRouter()
const orderHistory = useOrderHistory()

interface Pay {
    categoryId?: number,
    goodsId?: number,
    contact?: string
    quantity: number
    pay?: PayType
}

const form = reactive<Pay>({
    quantity: 1
})

const formRef = ref<FormInstance>()

const rule = reactive<FormRules>({
    categoryId: [{required: true, message: "请选择分类", trigger: "blur"}],
    goodsId: [{required: true, message: "请选择商品", trigger: "blur"}],
    contact: [{required: true, message: "请输入联系方式", trigger: "blur"}],
    pay: [{required: true, message: "请选择支付方式", trigger: "blur"}],
    quantity: [{required: true, message: "请输入购买数量", trigger: "blur"}]
})

const submit = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            post<IOrder>(`/pay/order?goods=${form.goodsId}`, form).then(r => {
                if (r.code == 200) {
                    orderHistory.pushOrder(r.metadata.id!)
                    ElMessageBox.alert(`订单号${r.metadata.id}`, '支付成功后使用请使用订单号查询状态', {
                        confirmButtonText: '确认',
                        callback: () => {
                            if (r.metadata.payType == PayType.ALIPAY) {
                                post<string>(`/pay/alipay?order=${r.metadata.id}`).then(pay => {
                                    if (pay.code === 200) {
                                        alipay(pay.metadata)
                                    }
                                })
                            }
                        }
                    })
                }
            })
        } else {
            return false
        }
    })
}

const category = ref<IPage<ICategory>>()
const store = ref<IStore>()

get<IStore>(`/merchant/store?path=${props.path}`).then(storeR => {
    if (storeR.metadata) {
        store.value = storeR.metadata
        get<IPage<ICategory>>(`/commodity/category?&user=${storeR.metadata.userId}`).then(r => {
            category.value = r.metadata
        })
    }
})

const goods = ref<IPage<IGoods>>()
const categoryChange = (value: number) => {
    get<IPage<IGoods>>(`/commodity/goods${buildParameter({
        user: store.value?.userId,
        category: value
    })}`).then(r => {
        goods.value = r.metadata
    })
}

const currentGoods = ref<IGoods>()
const goodsChange = (value: number) => {
    get<IGoods>(`/commodity/goods/${value}`).then(r => {
        currentGoods.value = r.metadata
    })
}

const showOrderHistory = ref(false)
const orderId = ref()

const findOrder = () => {
    get<IOrder>(`/pay/order/${orderId.value}`).then(r => {
        if (r.code === 200) {

            switch (r.metadata.status) {
                case OrderStatusType.CLOSED:
                    ElMessageBox.alert("交易已关闭", '消息', {
                        confirmButtonText: '确认',
                        type: "error"
                    })
                    break;
                case OrderStatusType.FINISHED:
                    ElMessageBox.alert("交易已完成", '消息', {
                        confirmButtonText: '确认',
                        type: "info"
                    })
                    break;
                case OrderStatusType.SUCCESS:
                    ElMessageBox({
                        title: "交易成功",
                        message: h(Kamis, {
                            data: r.metadata.kamis
                        })
                    })
                    break;
                case OrderStatusType.WAIT_PAY:
                case OrderStatusType.WAIT_CREATE:
                    ElMessageBox.confirm('还没有支付要去支付吗?', '警告',
                        {
                            confirmButtonText: '确认',
                            cancelButtonText: '取消',
                            type: 'warning',
                        }
                    )
                        .then(() => {
                            post<string>(`/pay/alipay?order=${r.metadata.id}`).then(pay => {
                                if (pay.code === 200) {
                                    alipay(pay.metadata)
                                }
                            })
                        })
                        .catch(() => {
                            ElMessage({
                                type: 'info',
                                message: '取消支付',
                            })
                        })
                    break;
            }
        }
    })
}

const alipay = (page: string) => {
    const alipayForm = document.getElementsByTagName('alipay')
    if (alipayForm.length) {
        document.body.removeChild(alipayForm[0])
    }
    const div = document.createElement('alipay')
    div.innerHTML = page
    document.body.appendChild(div)
    document.forms[1].setAttribute('target', '_blank')
    document.forms[1].submit()
}
</script>

<template>
  <div v-if="store">
    <el-form label-position="top" :model="form" ref="formRef" :rules="rule">
      <div class="flex-column" style="margin: 5rem;gap: 1rem">

        <el-card>
          <div class="flex justify-between">
            <el-button type="primary" @click="showOrderHistory = true">历史订单</el-button>
            <div class="flex" style="gap: 1rem">
              <el-input style="width: 320px;" placeholder="请输入订单号"
                        :prefix-icon="Search" v-model="orderId"/>
              <el-button type="primary" @click="findOrder">查询</el-button>
            </div>
          </div>
        </el-card>
        <el-card>
          <template #header>
            商家信息
          </template>
          <div class="flex-column" style="gap: 1rem">
            <div class="flex align-center">
              <el-button type="primary" text @click="router.push({name:'merchant',params:{id:store.userId}})">
                {{ store.name }}
              </el-button>
              <el-tag class="ml-2" type="danger" v-if="store.closing">打烊</el-tag>
              <el-tag class="ml-2" type="success" v-else>营业</el-tag>
            </div>
            <el-alert type="warning" :closable="false" v-if="store.user?.info?.bulletin">
              {{ store.user.info.bulletin }}
            </el-alert>
          </div>
        </el-card>
        <el-card>
          <template #header>
            选择
          </template>
          <el-form-item prop="categoryId" label="分类">
            <el-radio-group v-model="form.categoryId" size="large" v-if="category" @change="categoryChange">
              <el-radio-button :key="c.id" :label="c.id!" v-for="c in category.content">
                <template #default>
                  {{ c.name }}
                </template>
              </el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="goodsId" label="商品">
            <el-radio-group v-model="form.goodsId" size="large" v-if="goods" @change="goodsChange">
              <el-radio-button :key="g.id" :label="g.id!" v-for="g in goods.content">
                <template #default>
                  <div>{{ g.name }}</div>
                  <div>剩余:{{ g.stock ?? "0" }}</div>
                </template>
              </el-radio-button>
            </el-radio-group>
            <el-alert type="success" :closable="false" v-if="currentGoods">
              <template #title>
                <el-text class="mx-1" type="success">当前选择的商品:</el-text>
                <el-text class="mx-1" type="danger">{{ currentGoods.name }}</el-text>
              </template>
              {{ currentGoods.description }}
            </el-alert>
          </el-form-item>
        </el-card>
        <el-card>
          <template #header>
            购买
          </template>
          <el-form-item prop="contact" label="联系方式">
            <el-input v-model="form.contact"/>
          </el-form-item>
          <el-form-item prop="pay" label="支付方式">
            <el-radio-group v-model="form.pay" size="large">
              <el-radio-button :label="PayType.ALIPAY">支付宝</el-radio-button>
              <el-radio-button :label="PayType.WECHAT" disabled>微信</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="quantity" label="数量">
            <el-input-number v-model="form.quantity" :min="1" :max="10"/>
            <el-alert type="success" :closable="false" v-if="currentGoods">
              <el-text class="mx-1" type="success">合计:</el-text>
              <el-text class="mx-1" type="danger">{{ currentGoods.price * form.quantity }}</el-text>
            </el-alert>
          </el-form-item>
          <el-form-item>
            <div class="flex" style="width: 100%;flex-direction: row-reverse">
              <el-button type="primary" @click="submit(formRef)">
                确定订单
              </el-button>
            </div>
          </el-form-item>
        </el-card>
      </div>
    </el-form>
  </div>
  <div class="flex justify-center align-center" style="height: 100vh;" v-else>
    <el-empty :image-size="200" description="店铺不存在"/>
  </div>
  <el-dialog v-model="showOrderHistory">
    <order-history/>
  </el-dialog>
</template>

<style scoped></style>

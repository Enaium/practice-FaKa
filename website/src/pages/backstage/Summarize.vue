<script setup lang="ts">
import {ref} from "vue";
import {get, post} from "@/util/request";
import {useUserStatus} from "@/store";
import {ElMessage, ElMessageBox} from "element-plus";

const userStatus = useUserStatus();

const balance = ref()

get(`/merchant/user/${userStatus.id}/wallet/balance`).then(r => {
    balance.value = r.metadata
})

const withdrawal = () => {
    ElMessageBox.confirm('你确定要提现吗?', '警告',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            post(`/merchant/user/${userStatus.id}/wallet/withdrawal`).then(r => {
                if (r.code === 200) {
                    ElMessage({message: "提现成功", type: "success"})
                }
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消提现',
            })
        })
}
</script>

<template>
  <div v-if="balance !== undefined">
    <div>信息总汇</div>
    <el-divider/>
    <div style="display: flex;">
      <el-card style="width: 200px;background: #409EFC;color: white;font-weight: 800">
        <div>账户余额:{{ balance }}</div>
      </el-card>
      <div style="display: flex;flex-direction: column-reverse">
        <el-button type="primary" text @click="withdrawal">提现</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped></style>

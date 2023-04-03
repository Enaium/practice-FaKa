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
import {ref} from "vue";
import {useUserStatus} from "@/store";
import {IStore} from "@/util/model";
import {useRouter} from "vue-router";
import {getUser} from "@/util/request";

const router = useRouter()

const userStatus = useUserStatus()

const store = ref<IStore>()

getUser(userStatus.id).then(r => {
    store.value = r.metadata.store
})
</script>

<template>
  <div v-if="store">
    <div>店铺信息</div>
    <el-divider/>
    <div class="flex align-center" style="flex-wrap: wrap;gap: 1rem">
      <span>名称:{{ store.name }}</span>
      <span>地址:</span>
      <el-button type="primary" text @click="router.push({name:'purchase',params:{path:store.path}})">
        {{ store.path }}
      </el-button>
      <span>打烊:{{ store.closing }}</span>
    </div>
  </div>
</template>

<style scoped>

</style>

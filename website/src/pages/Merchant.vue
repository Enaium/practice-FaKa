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
import {computed, ref} from "vue";
import {IUser} from "@/util/model";
import {getUser} from "@/util/request";
import Logo from "@/assets/vue.svg"
import {useRouter} from "vue-router";

const props = defineProps<{
    id: string
}>()

const router = useRouter()

const user = ref<IUser>()

getUser(props.id).then(r => {
    user.value = r.metadata
})

const avatar = computed(() => {
    return user.value?.info.avatarUrl || Logo
})

const website = computed(() => {
    if (user.value?.info.website?.startsWith("http")) {
        return user.value?.info.website
    } else {
        return user.value?.info.website ? `https://${user.value?.info.website}` : null
    }
})
</script>

<template>
  <el-card v-if="user" style="margin: 5rem">
    <template #header>
      <div class="flex justify-between">
        <div>商家信息</div>
        <el-button type="primary" @click="router.push({name:'purchase',params:{path:user.store.path}})">
          访问店铺
        </el-button>
      </div>
    </template>
    <div class="flex-column align-center" style="gap: 1rem">
      <el-avatar :size="80" :src="avatar"/>
      <div class="flex justify-center align-center" style="gap: 1rem">
        <div>名称:{{ user.info.name }}</div>
        <el-divider direction="vertical"/>
        <div>联系方式:{{ user.info.contact }}</div>
        <el-divider direction="vertical"/>
        <div>
          状态:
          <el-tag class="ml-2" type="danger" v-if="user.store.closing">打烊</el-tag>
          <el-tag class="ml-2" type="success" v-else>营业</el-tag>
        </div>
      </div>
      <div v-if="user.info.bulletin">公告:{{ user.info.bulletin }}</div>
      <div v-if="website">网站: <a :href="website" target="_blank">{{ website }}</a>
      </div>
    </div>
  </el-card>
</template>

<style scoped>

</style>

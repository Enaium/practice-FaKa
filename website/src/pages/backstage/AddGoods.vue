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

import {IGoods} from "@/util/model";
import {reactive} from "vue";
import GoodsForm from "@/components/backstage/GoodsForm.vue";

const goodsForm = reactive<IGoods>({})

</script>

<template>
  <div>添加商品</div>
  <el-divider/>
  <goods-form :form="goodsForm"/>
</template>

<style scoped>

</style>

<script setup lang="ts">

import Menu from "@/components/backstage/Menu.vue";</script>

<template>
  <div class="common-layout">
    <el-container style="height: 100vh">
      <el-aside style="width: 200px">
        <Menu style="height: 100%"/>
      </el-aside>
      <el-main>
        <el-card>
          <router-view/>
        </el-card>
      </el-main>
    </el-container>
  </div>
</template>

<style scoped>

</style>

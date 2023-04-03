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
import {getUser} from "@/util/request";
import {computed, reactive, ref} from "vue";
import {ContactType, IUser} from "@/util/model";
import Logo from "@/assets/vue.svg"
import {contactTypeParse} from "@/util";
import {CreditCard, Iphone, Message} from "@element-plus/icons-vue";
import {ElMessage, FormInstance, FormRules} from "element-plus";
import http from "@/util/http";
import {useUserStatus} from "@/store";

const userStatus = useUserStatus();

const user = ref<IUser>()

getUser(userStatus.id).then(r => {
    user.value = r.metadata
})

const avatar = computed(() => {
    return user.value?.info.avatarUrl || Logo
})

const infoRef = ref<FormInstance>()
const phoneRef = ref<FormInstance>()
const emailRef = ref<FormInstance>()
const wechatRef = ref<FormInstance>()
const alipayRef = ref<FormInstance>()
const avatarRef = ref<FormInstance>()

const update = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate((valid) => {
        if (valid) {
            http.patch(`/merchant/user/${user.value?.id}`, user.value?.info).then(r => {
                if (r.data.code === 200) {
                    ElMessage({message: "更新成功", type: "success"})
                }
            })
        } else {
            return false
        }
    })
}

const validateTencentQQ = (rule: any, value: any, callback: any) => {
    if (!value || value.length < 6 || value.length > 11) {
        callback(new Error("请输入正确的QQ"))
    } else {
        callback()
    }
}

const validateTencentQQGroup = (rule: any, value: any, callback: any) => {
    if (!value || value.length < 6 || value.length > 10) {
        callback(new Error("请输入正确的QQ群"))
    } else {
        callback()
    }
}
const validateWechat = (rule: any, value: any, callback: any) => {
    if (!value || value.length < 6 || value.length > 20) {
        callback(new Error("请输入正确的微信"))
    } else {
        callback()
    }
}

const validatePhone = (rule: any, value: any, callback: any) => {
    if (!value || value.length != 11) {
        callback(new Error("请输入正确的手机"))
    } else {
        callback()
    }
}

const validateEmail = (rule: any, value: any, callback: any) => {
    if (!value || !(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value) && value.length < 32)) {
        callback(new Error("请输入正确的邮箱"))
    } else {
        callback()
    }
}

const validateCustom = (rule: any, value: any, callback: any) => {
    if (!value || value.length > 50) {
        callback(new Error("请输入正确的自定义"))
    } else {
        callback()
    }
}

const validateRealName = (rule: any, value: any, callback: any) => {
    if (!value || value.length < 2 || value.length > 8) {
        callback(new Error("请输入正确的名字"))
    } else {
        callback()
    }
}

const validateAlipay = (rule: any, value: any, callback: any) => {
    if (!value || value.length > 32) {
        callback(new Error("请输入正确的账号"))
    } else {
        callback()
    }
}

const validateAvatar = (rule: any, value: any, callback: any) => {
    if (!value || !/(http|https):\/\/([\w.]+\/?)\S*/ig.test(value)) {
        callback(new Error("请输入正确的头像地址"))
    } else {
        callback()
    }
}

const validateContact = (rule: any, value: any, callback: any) => {
    switch (user.value?.info.contactType) {
        case ContactType.TENCENT_QQ:
            validateTencentQQ(rule, value, callback)
            break;
        case ContactType.TENCENT_QQ_GROUP:
            validateTencentQQGroup(rule, value, callback)
            break;
        case ContactType.WECHAT:
            validateWechat(rule, value, callback)
            break;
        case ContactType.PHONE:
            validatePhone(rule, value, callback)
            break;
        case ContactType.EMAIL:
            validateEmail(rule, value, callback)
            break;
        case ContactType.CUSTOM:
            validateCustom(rule, value, callback)
            break;
        case ContactType.NONE:
            if (user.value.info) {
                user.value.info.contact = undefined
            }
            callback()
            break;
    }
}

const infoRule = reactive<FormRules>({
    contact: [
        {validator: validateContact, trigger: 'blur'},
    ]
})

const phoneRule = reactive<FormRules>({
    phoneNumber: [
        {validator: validatePhone, trigger: 'blur'},
    ]
})

const emailRule = reactive<FormRules>({
    email: [
        {validator: validateEmail, trigger: 'blur'},
    ]
})

const wechatRule = reactive<FormRules>({
    wechatNumber: [
        {validator: validateWechat, trigger: 'blur'},
    ]
})

const alipayRule = reactive<FormRules>({
    realName: [
        {validator: validateRealName, trigger: 'blur'},
    ],
    alipayNumber: [
        {validator: validateAlipay, trigger: 'blur'},
    ]
})

const avatarRule = reactive<FormRules>({
    avatarUrl: [
        {validator: validateAvatar, trigger: 'blur'},
    ]
})


const showPhone = ref(false)
const showEmail = ref(false)
const showWechat = ref(false)
const showAlipay = ref(false)
const showAvatar = ref(false)

</script>

<template>
  <div v-if="user">
    <div>商户信息</div>
    <el-divider/>
    <div class="flex" style="gap: 1rem">
      <el-avatar :size="80" :src="avatar" @click="showAvatar = !showAvatar"/>
      <div style="display: flex;flex-direction: column;justify-content: space-between">

        <div>{{ user.username }}</div>

        <div class="flex" style="gap: 1rem">

          <div class="flex align-center">
            <el-icon color="#409EFC" class="no-inherit">
              <Iphone/>
            </el-icon>
            <div>手机号码:</div>
            <div v-if="user.info.phoneNumber">{{ user.info.phoneNumber }}</div>
            <el-button text @click="showPhone = !showPhone" v-else>点我绑定</el-button>
          </div>

          <div class="flex align-center">
            <el-icon color="#409EFC" class="no-inherit">
              <Message/>
            </el-icon>
            <div>邮箱:</div>
            <div v-if="user.info.email">{{ user.info.email }}</div>
            <el-button text @click="showEmail = !showEmail" v-else>点我绑定</el-button>
          </div>

          <div class="flex align-center">
            <el-icon color="#409EFC" class="no-inherit">
              <Message/>
            </el-icon>
            <div>微信:</div>
            <div v-if="user.info.wechatNumber">{{ user.info.wechatNumber }}</div>
            <el-button text @click="showWechat = !showWechat" v-else>点我绑定</el-button>
          </div>

          <div class="flex align-center">
            <el-icon color="#409EFC" class="no-inherit">
              <CreditCard/>
            </el-icon>
            <div>支付宝:</div>
            <div v-if="user.info.alipayNumber">{{ user.info.alipayNumber }}</div>
            <el-button text @click="showAlipay = !showAlipay" v-else>点我绑定</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-divider/>

    <el-form :model="user.info" label-width="100" ref="infoRef" :rules="infoRule">
      <el-form-item prop="contactType" label="商户联系方式">
        <el-select v-model="user.info.contactType" class="m-2" placeholder="Select" size="large">
          <el-option v-for="(item, index) in ContactType" :key="index" :label="contactTypeParse(item)" :value="item"/>
        </el-select>
      </el-form-item>
      <el-form-item prop="contact">
        <el-input v-model="user.info.contact"/>
      </el-form-item>
      <el-form-item prop="name" label="商家名称">
        <el-input v-model="user.info.name"/>
      </el-form-item>
      <el-form-item prop="website" label="店铺网站">
        <el-input v-model="user.info.website"/>
      </el-form-item>
      <el-form-item prop="payTip" label="付款提示">
        <el-input v-model="user.info.payTip" type="textarea"/>
      </el-form-item>
      <el-form-item prop="bulletin" label="店铺公告">
        <el-input v-model="user.info.bulletin" type="textarea"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="update(infoRef)">
          确认修改
        </el-button>
      </el-form-item>
    </el-form>

    <el-dialog v-model="showPhone" title="绑定手机">
      <el-form :model="user.info" ref="phoneRef" :rules="phoneRule">
        <el-form-item prop="phoneNumber" label="手机">
          <el-input v-model="user.info.phoneNumber"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update(phoneRef)">
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-model="showEmail" title="绑定邮箱">
      <el-form :model="user.info" ref="emailRef" :rules="emailRule">
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="user.info.email"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update(emailRef)">
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-model="showWechat" title="绑定微信">
      <el-form :model="user.info" ref="wechatRef" :rules="wechatRule">
        <el-form-item prop="wechatNumber" label="微信">
          <el-input v-model="user.info.wechatNumber"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update(wechatRef)">
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-model="showAlipay" title="绑定支付宝">
      <el-form :model="user.info" ref="alipayRef" :rules="alipayRule">
        <el-form-item prop="realName" label="姓名">
          <el-input v-model="user.info.realName"/>
        </el-form-item>
        <el-form-item prop="alipayNumber" label="账号">
          <el-input v-model="user.info.alipayNumber"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update(alipayRef)">
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-model="showAvatar" title="设置头像">
      <el-form :model="user.info" ref="avatarRef" :rules="avatarRule">
        <el-form-item prop="avatarUrl" label="头像地址">
          <el-input v-model="user.info.avatarUrl"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="update(avatarRef)">
            确认修改
          </el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<style scoped></style>

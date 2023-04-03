/*
 * Copyright 2023 Enaium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.enaium.faka.model.type

import org.babyfish.jimmer.sql.EnumItem

/**
 * @author Enaium
 */
enum class ContactType {
    /**
     * 腾讯QQ
     */
    @EnumItem(name = "tencent_qq")
    TENCENT_QQ,

    /**
     * 腾讯QQ群
     */
    @EnumItem(name = "tencent_qq_group")
    TENCENT_QQ_GROUP,

    /**
     * 微信
     */
    @EnumItem(name = "wechat")
    WECHAT,

    /**
     * 手机
     */
    @EnumItem(name = "phone")
    PHONE,

    /**
     * 邮箱
     */
    @EnumItem(name = "email")
    EMAIL,

    /**
     * 自定义
     */
    @EnumItem(name = "custom")
    CUSTOM,

    /**
     * 无
     */
    @EnumItem(name = "none")
    NONE
}
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

package cn.enaium.faka.model.post

import cn.enaium.faka.model.type.ContactType

/**
 * @author Enaium
 */
data class UserPost(
    val avatarUrl: String?,
    val phoneNumber: Long?,
    val wechatNumber: String?,
    val email: String?,
    val contactType: ContactType?,
    val contact: String?,
    val name: String?,
    val website: String?,
    val payTip: String?,
    val bulletin: String?
)

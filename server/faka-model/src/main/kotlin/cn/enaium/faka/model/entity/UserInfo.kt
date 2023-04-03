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

package cn.enaium.faka.model.entity

import cn.enaium.faka.model.type.ContactType
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.util.*
import javax.validation.constraints.Email
import javax.validation.constraints.Size

/**
 * @author Enaium
 */
@Entity
@Table(name = "t_user_info")
interface UserInfo {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID

    /**
     * 用户ID
     */
    @Key
    @Column(name = "user_id")
    val userId: UUID

    /**
     * 用户
     */
    @OneToOne
    val user: User

    /**
     *  头像路径
     */
    @get:Size(max = 50)
    @Column(name = "avatar_url")
    val avatarUrl: String?

    /**
     * 电话号码
     */
    @Column(name = "phone_number")
    val phoneNumber: Long?

    /**
     * 微信号
     */
    @get:Size(min = 6, max = 20)
    @Column(name = "wechat_number")
    val wechatNumber: String?

    /**
     * 邮箱
     */
    @get:Email
    val email: String?

    /**
     * 联系方式
     */
    @Column(name = "contact_type")
    val contactType: ContactType

    /**
     * 联系
     */
    @get:Size(max = 50)
    val contact: String?

    /**
     * 商家名称
     */
    @get:Size(max = 10)
    val name: String?

    /**
     * 网站
     */
    @get:Size(max = 100)
    val website: String?

    /**
     * 店铺公告
     */
    @get:Size(max = 500)
    val bulletin: String?

    /**
     * 支付提示
     */
    @get:Size(max = 500)
    @Column(name = "pay_tip")
    val payTip: String?

    /**
     * 真实名字
     */
    @get:Size(min = 2, max = 8)
    @Column(name = "real_name")
    val realName: String?

    /**
     * 支付宝账号
     */
    @get:Size(max = 32)
    @Column(name = "alipay_number")
    val alipayNumber: String?
}
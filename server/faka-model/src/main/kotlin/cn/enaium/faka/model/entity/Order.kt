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

import cn.enaium.faka.model.type.OrderPayType
import cn.enaium.faka.model.type.OrderStatusType
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Max
import javax.validation.constraints.Min

/**
 * @author Enaium
 */
@Entity
@Table(name = "t_order")
interface Order {
    /**
     * 用户主键
     */
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID

    @Column(name = "goods_id")
    val goodsId: UUID

    @OneToOne
    val goods: Goods

    @Column(name = "user_id")
    val userId: UUID

    @OneToOne
    val user: User

    @ManyToMany
    @JoinTable(name = "t_order_to_kami", joinColumnName = "order_id", inverseJoinColumnName = "kami_id")
    val kamis: List<Kami>

    val status: OrderStatusType

    @Column(name = "pay_type")
    val payType: OrderPayType

    @get:DecimalMax(value = "999999.0")
    @get:DecimalMin(value = "0.1")
    val amount: BigDecimal

    @get:Max(value = 10)
    @get:Min(value = 1)
    val quantity: Int

    @Column(name = "create_time")
    val createTime: LocalDateTime

    @Column(name = "update_time")
    val updateTime: LocalDateTime
}
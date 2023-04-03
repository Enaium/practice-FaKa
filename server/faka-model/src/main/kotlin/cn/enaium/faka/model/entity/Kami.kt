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

import cn.enaium.faka.model.type.KamiStatusType
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@Entity
@Table(name = "t_kami")
interface Kami {
    /**
     * 卡密主键
     */
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID

    @Column(name = "goods_id")
    val goodsId: UUID

    @ManyToOne
    val goods: Goods

    @ManyToMany(mappedBy = "kamis")
    val orders: List<Order>

    @Column(name = "user_id")
    val userId: UUID

    val content: String

    val status: KamiStatusType

    @Column(name = "create_time")
    val createTime: LocalDateTime

    @Column(name = "update_time")
    val updateTime: LocalDateTime
}
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

import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.Size

/**
 * @author Enaium
 */
@Entity
@Table(name = "t_category")
interface Category {
    /**
     * 分类主键
     */
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    val userId: UUID

    /**
     * 商品
     */
    @OneToMany(mappedBy = "category")
    val goods: List<Goods>

    /**
     * 分类名称
     */
    @get:Size(min = 2, max = 40)
    val name: String

    /**
     * 优先级
     */
    val priority: Int

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    val createTime: LocalDateTime

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    val updateTime: LocalDateTime
}
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

import cn.enaium.faka.model.resolver.GoodsStockResolver
import cn.enaium.faka.model.type.GoodsType
import cn.enaium.faka.model.type.SaleOrderType
import org.babyfish.jimmer.sql.*
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Size

/**
 * @author Enaium
 */
@Entity
@Table(name = "t_goods")
interface Goods {
    /**
     * 商品主键
     */
    @Id
    @GeneratedValue(generatorType = UUIDIdGenerator::class)
    val id: UUID

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    val userId: UUID

    @OneToOne
    val user: User

    /**
     * 分类ID
     */
    @Column(name = "category_id")
    val categoryId: UUID

    /**
     * 分类
     */
    @ManyToOne
    val category: Category?

    /**
     * 商品名称
     */
    @get:Size(min = 2, max = 40)
    val name: String

    /**
     * 优先级
     */
    val priority: Int

    /**
     * 商品价格
     */
    @get:DecimalMax(value = "20000.0")
    @get:DecimalMin(value = "0.1")
    val price: BigDecimal

    /**
     * 商品描述
     */
    @get:Size(min = 10, max = 500)
    val description: String

    /**
     * 商品类型
     */
    val type: GoodsType

    /**
     * 出售顺序
     */
    @Column(name = "sale_order")
    val saleOrder: SaleOrderType

    @OneToMany(mappedBy = "goods")
    val kamis: List<Kami>

    @Transient(GoodsStockResolver::class)
    val stock: Long?

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
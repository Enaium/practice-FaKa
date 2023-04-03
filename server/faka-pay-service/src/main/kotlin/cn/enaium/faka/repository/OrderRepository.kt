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

package cn.enaium.faka.repository

import cn.enaium.faka.model.entity.Order
import cn.enaium.faka.model.entity.by
import cn.enaium.faka.model.entity.orders
import cn.enaium.faka.model.entity.status
import cn.enaium.faka.model.type.OrderStatusType
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.stereotype.Repository
import java.util.*

/**
 * @author Enaium
 */
@Repository
interface OrderRepository : KRepository<Order, UUID> {
    fun findFinalById(id: UUID): Order? = findNullable(id, newFetcher(Order::class).by {
        allScalarFields()
        goods {
            name()
            price()
        }
        user {
            store {
                path()
            }
            info {
                payTip()
            }
        }
        kamis({
            filter {
                //只有支付完成时才可以查看卡密
                where(table.asTableEx().orders.status eq OrderStatusType.SUCCESS)
            }
        }) {
            allScalarFields()
        }
    })
}
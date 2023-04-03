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

package cn.enaium.faka.model.resolver

import cn.enaium.faka.model.entity.*
import cn.enaium.faka.model.type.KamiStatusType
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.KTransientResolver
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author Enaium
 */
@Component
class GoodsStockResolver(val sql: KSqlClient) : KTransientResolver<UUID, Long> {
    override fun resolve(ids: Collection<UUID>): Map<UUID, Long> {
        return sql.createQuery(Goods::class) {
            where(
                table.asTableEx().kamis.id valueIn subQuery(Kami::class) {
                    where(table.goods.id valueIn ids, table.status eq KamiStatusType.SELL)
                    select(table.id)
                }
            )
            groupBy(table.id)
            select(table.id, count(table.asTableEx().kamis.id))
        }.execute().associateBy({ it._1 }) { it._2 }
    }
}
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

package cn.enaium.faka.mq.subscriber

import cn.enaium.faka.configuration.EXCHANGE_NAME
import cn.enaium.faka.model.entity.Kami
import cn.enaium.faka.model.entity.Order
import cn.enaium.faka.model.entity.by
import cn.enaium.faka.model.type.GoodsType
import cn.enaium.faka.model.type.KamiStatusType
import cn.enaium.faka.model.type.SaleOrderType
import cn.enaium.faka.mq.message.OrderMessage
import cn.enaium.faka.repository.KamiRepository
import cn.enaium.faka.repository.OrderRepository
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.amqp.rabbit.annotation.*
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * @author Enaium
 */
@Component
@Transactional
@RabbitListener(
    bindings = [QueueBinding(
        value = Queue(name = OrderSubscriber.ORDER),
        exchange = Exchange(name = EXCHANGE_NAME)
    )]
)
class OrderSubscriber(val orderRepository: OrderRepository, val kamiRepository: KamiRepository, val sql: KSqlClient) {

    companion object {
        const val ORDER = "faka.queue.pay.order"
    }


    @RabbitHandler
    fun order(message: OrderMessage) {
        val order = orderRepository.findNullable(message.order, newFetcher(Order::class).by {
            allScalarFields()
            goods {
                allScalarFields()
            }
        })!!

        val kamis = kamiRepository.findByGoodsIdAndStatusOrderByCreateTime(order.goodsId, KamiStatusType.SELL)

        when (order.goods.type) {
            GoodsType.SINGLE -> {
                sql.getAssociations(Order::kamis).save(order.id, kamis[0].id)
            }

            GoodsType.MULTI -> {
                for (i in 0 until order.quantity) {
                    val kami = when (order.goods.saleOrder) {
                        SaleOrderType.NEW -> kamis[i]
                        SaleOrderType.OLD -> kamis[kamis.size - 1 - i]
                    }

                    kamiRepository.update(new(Kami::class).by {
                        id = kami.id
                        status = KamiStatusType.LOCK
                    })
                    sql.getAssociations(Order::kamis).save(order.id, kami.id)
                }
            }
        }

    }
}
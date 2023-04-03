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
import cn.enaium.faka.model.entity.Order
import cn.enaium.faka.model.entity.by
import cn.enaium.faka.mq.message.AlipayNotifyMessage
import cn.enaium.faka.repository.KamiRepository
import cn.enaium.faka.repository.OrderRepository
import cn.enaium.faka.service.PayService
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
        value = Queue(name = AlipayNotifySubscriber.NOTIFY),
        exchange = Exchange(name = EXCHANGE_NAME)
    )]
)
class AlipayNotifySubscriber(
    val orderRepository: OrderRepository,
    val payService: PayService
) {
    companion object {
        const val NOTIFY = "faka.queue.pay.alipay.notify"
    }

    @RabbitHandler
    fun notify(message: AlipayNotifyMessage) {
        val order = orderRepository.findNullable(message.order, newFetcher(Order::class).by {
            kamis {
                allScalarFields()
            }
        }) ?: return

        payService.updateOrderStatus(order, message.status)
    }
}
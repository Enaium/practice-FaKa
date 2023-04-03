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

package cn.enaium.faka.service

import cn.dev33.satoken.stp.StpUtil
import cn.enaium.faka.model.entity.Kami
import cn.enaium.faka.model.entity.Order
import cn.enaium.faka.model.entity.WalletRecord
import cn.enaium.faka.model.entity.by
import cn.enaium.faka.model.type.KamiStatusType
import cn.enaium.faka.model.type.OrderStatusType
import cn.enaium.faka.repository.KamiRepository
import cn.enaium.faka.repository.OrderRepository
import cn.enaium.faka.repository.WalletRecordRepository
import org.babyfish.jimmer.kt.new
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@Transactional
@Service
class PayService(
    val orderRepository: OrderRepository,
    val kamiRepository: KamiRepository,
    val walletRecordRepository: WalletRecordRepository
) {
    fun updateOrderStatus(order: Order, orderStatus: String) {
        //更新订单状态
        orderRepository.update(new(Order::class).by {
            id = order.id
            status = when (orderStatus) {
                "WAIT_BUYER_PAY" -> OrderStatusType.WAIT_PAY
                "TRADE_CLOSED" -> OrderStatusType.CLOSED
                "TRADE_SUCCESS" -> {
                    OrderStatusType.SUCCESS
                }

                "TRADE_FINISHED" -> OrderStatusType.FINISHED
                else -> {
                    OrderStatusType.WAIT_PAY
                }
            }
        })
        //更新该订单所有卡密为已出售
        when (orderStatus) {
            "TRADE_SUCCESS", "TRADE_FINISHED" -> {
                order.kamis.forEach {
                    kamiRepository.update(new(Kami::class).by {
                        id = it.id
                        status = KamiStatusType.SOLD
                    })
                }
            }
        }

        //更新钱包记录
        when (orderStatus) {
            "TRADE_SUCCESS" -> {
                changeAmount(order.amount)
            }
        }
    }

    fun changeAmount(change: BigDecimal) {
        walletRecordRepository.insert(new(WalletRecord::class).by {
            userId = UUID.fromString(StpUtil.getLoginIdAsString())
            amount = change
            createTime = LocalDateTime.now()
        })
    }
}
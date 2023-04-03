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

package cn.enaium.faka.controller

import cn.enaium.faka.model.entity.*
import cn.enaium.faka.model.post.OrderPost
import cn.enaium.faka.model.type.*
import cn.enaium.faka.mq.message.OrderMessage
import cn.enaium.faka.mq.subscriber.OrderSubscriber
import cn.enaium.faka.repository.GoodsRepository
import cn.enaium.faka.repository.KamiRepository
import cn.enaium.faka.repository.OrderRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.service.AlipayService
import cn.enaium.faka.service.PayService
import cn.enaium.faka.service.json
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@Transactional
@RestController
@RequestMapping("/order")
class OrderController(
    val goodsRepository: GoodsRepository,
    val orderRepository: OrderRepository,
    val kamiRepository: KamiRepository,
    val sql: KSqlClient,
    val alipayService: AlipayService,
    val payService: PayService,
    val rabbitTemplate: RabbitTemplate
) {
    /**
     * 创建订单
     *
     * @param goods 商品ID
     * @param param 参数
     */
    @PostMapping
    fun order(@RequestParam goods: UUID, @RequestBody param: OrderPost): Result<Order?> {

        val findNullable =
            goodsRepository.findNullable(goods, newFetcher(Goods::class).by {
                allScalarFields()
                user {
                    store {
                        closing()
                    }
                }
            }) ?: return Result.Builder.fail(status = Result.Status.GOODS_DOESNT_EXIST)

        val store = findNullable.user.store
        store ?: return Result.Builder.fail(status = Result.Status.STORE_DOESNT_EXIST)
        if (store.closing) {
            return Result.Builder.fail(status = Result.Status.STORE_CLOSING)
        }

        val kamis = kamiRepository.findByGoodsIdAndStatusOrderByCreateTime(findNullable.id, KamiStatusType.SELL)

        if (kamis.size < param.quantity || kamis.isEmpty()) {
            return Result.Builder.fail(status = Result.Status.GOODS_SOLD_OUT)
        }

        val order = orderRepository.insert(new(Order::class).by {
            goodsId = goods
            userId = findNullable.userId
            payType = OrderPayType.ALIPAY
            amount = findNullable.price.multiply(param.quantity.toBigDecimal())
            quantity = param.quantity
            status = OrderStatusType.WAIT_CREATE
            createTime = LocalDateTime.now()
            updateTime = LocalDateTime.now()
        })

        rabbitTemplate.convertAndSend(OrderSubscriber.ORDER, OrderMessage(order.id))


        return Result.Builder.success(metadata = order)
    }


    /**
     * 关闭订单
     *
     * @param order 订单
     */
    @PatchMapping("/close")
    fun close(@RequestParam order: UUID): Result<Nothing?> {
        val o =
            orderRepository.findNullable(order) ?: return Result.Builder.fail(status = Result.Status.ORDER_DOESNT_EXIST)
        if (o.status != OrderStatusType.WAIT_CREATE) {
            alipayService.closeOrder(order.toString())
        }

        orderRepository.update(new(Order::class).by {
            id = o.id
            status = OrderStatusType.CLOSED
        })
        return Result.Builder.success()
    }

    /**
     * 订单退款
     *
     * @param order 订单
     */
    @PatchMapping("/refund")
    fun refund(@RequestParam order: UUID): Result<Nothing?> {
        val o =
            orderRepository.findNullable(order) ?: return Result.Builder.fail(status = Result.Status.ORDER_DOESNT_EXIST)
        if (o.status != OrderStatusType.SUCCESS) {
            return Result.Builder.fail(status = Result.Status.ORDER_UNPAID)
        }
        return Result.Builder.success()
    }

    /**
     * 获取订单信息
     *
     * @param id 订单ID
     */
    @GetMapping("/{id}")
    fun order(@PathVariable id: String): Result<Order?> {
        val uuid = try {
            UUID.fromString(id)
        } catch (e: Exception) {
            return Result.Builder.fail(status = Result.Status.ORDER_DOESNT_EXIST)
        }

        val order = (orderRepository.findFinalById(uuid)
            ?: return Result.Builder.fail(status = Result.Status.ORDER_DOESNT_EXIST))

        //当 订单关闭,订单完成,订单支付完成 时,直接返回订单信息,无需更新订单或卡密信息
        if (order.status == OrderStatusType.CLOSED || order.status == OrderStatusType.FINISHED || order.status == OrderStatusType.SUCCESS) {
            return Result.Builder.success(metadata = order)
        }

        val queryOrder = alipayService.queryOrder(id).json()

        if (!queryOrder.has("trade_status")) {
            return Result.Builder.success(message = queryOrder["sub_msg"].asText(), metadata = order)
        }

        payService.updateOrderStatus(order, queryOrder["trade_status"].asText())

        return Result.Builder.success(
            metadata = orderRepository.findFinalById(uuid)
                ?: return Result.Builder.fail(status = Result.Status.ORDER_DOESNT_EXIST)
        )
    }
}
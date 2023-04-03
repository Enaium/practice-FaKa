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

import cn.enaium.faka.model.entity.Order
import cn.enaium.faka.model.entity.by
import cn.enaium.faka.mq.message.AlipayNotifyMessage
import cn.enaium.faka.mq.subscriber.AlipayNotifySubscriber
import cn.enaium.faka.repository.KamiRepository
import cn.enaium.faka.repository.OrderRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.service.AlipayService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @author Enaium
 */
@Transactional
@RestController
@RequestMapping("/alipay")
class AlipayController(
    val orderRepository: OrderRepository,
    val alipayService: AlipayService,
    val rabbitTemplate: RabbitTemplate
) {
    /**
     * 创建订单
     *
     * @param order 订单
     */
    @PostMapping
    fun post(@RequestParam order: UUID): Result<String?> {
        val o =
            orderRepository.findNullable(order, newFetcher(Order::class).by {
                allScalarFields()
                goods {
                    name()
                }
            }) ?: return Result.Builder.fail(status = Result.Status.ORDER_DOESNT_EXIST)

        val createOrder = alipayService.createOrder(
            o.id.toString(),
            o.amount.toString(),
            o.goods.name
        )

        return Result.Builder.success(
            metadata = createOrder
        )
    }

    /**
     * 支付宝通知
     *
     * @param httpServletRequest 请求
     * @param httpServletResponse 响应
     */
    @PostMapping("/notify")
    fun notifyURL(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        val param = HashMap<String, String>()
        httpServletRequest.parameterMap.forEach { (k, v) ->
            param[k] = v.joinToString(",")
        }
        val check = alipayService.rsaCheckV1(
            param,
            StandardCharsets.UTF_8.name(),
        )

        if (!check) {
            httpServletResponse.writer.write("支付失败")
            return
        }

        val outTradeNo = param["out_trade_no"]
        val tradeStatus = param["trade_status"]

        rabbitTemplate.convertAndSend(
            AlipayNotifySubscriber.NOTIFY,
            AlipayNotifyMessage(UUID.fromString(outTradeNo), tradeStatus!!)
        )
        httpServletResponse.writer.close()
    }
}
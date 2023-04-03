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

import cn.enaium.faka.configuration.AlipayConfiguration
import com.alipay.api.AlipayResponse
import com.alipay.api.DefaultAlipayClient
import com.alipay.api.internal.util.AlipaySignature
import com.alipay.api.request.AlipayTradeCloseRequest
import com.alipay.api.request.AlipayTradePagePayRequest
import com.alipay.api.request.AlipayTradeQueryRequest
import com.alipay.api.request.AlipayTradeRefundRequest
import com.alipay.api.response.AlipayTradeQueryResponse
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.nio.charset.StandardCharsets

fun AlipayResponse.json(): JsonNode {
    return jacksonObjectMapper().readValue(body, JsonNode::class.java)["alipay_trade_query_response"]
}

/**
 * @author Enaium
 */
@Service
class AlipayService(private val configuration: AlipayConfiguration) {
    fun createOrder(outTradeNo: String, totalAmount: String, subject: String): String? {
        return defaultAlipayClient().pageExecute(AlipayTradePagePayRequest().apply {
            notifyUrl = configuration.notifyUrl
            returnUrl = configuration.returnUrl
            bizContent = jacksonObjectMapper().writeValueAsString(
                mapOf(
                    "out_trade_no" to outTradeNo,
                    "total_amount" to totalAmount,
                    "subject" to subject,
                    "product_code" to "FAST_INSTANT_TRADE_PAY"
                )
            )
        }).body
    }

    fun closeOrder(outTradeNo: String) {
        defaultAlipayClient().execute(AlipayTradeCloseRequest().apply {
            bizContent = jacksonObjectMapper().writeValueAsString(
                mapOf(
                    "out_trade_no" to outTradeNo
                )
            )
        })
    }

    fun refund(outTradeNo: String, amount: BigDecimal) {
        defaultAlipayClient().execute(AlipayTradeRefundRequest().apply {
            bizContent = jacksonObjectMapper().writeValueAsString(
                mapOf(
                    "out_trade_no" to outTradeNo,
                    "refund_amount" to amount.toString()
                )
            )
        })
    }

    fun queryOrder(outTradeNo: String): AlipayTradeQueryResponse {
        return defaultAlipayClient().execute(AlipayTradeQueryRequest().apply {
            bizContent = jacksonObjectMapper().writeValueAsString(
                mapOf(
                    "out_trade_no" to outTradeNo
                )
            )
        })
    }

    private fun defaultAlipayClient() = DefaultAlipayClient(
        configuration.gateway,
        configuration.appId,
        configuration.privateKey,
        "json",
        StandardCharsets.UTF_8.name(),
        configuration.alipayPublicKey,
        configuration.signType
    )

    fun rsaCheckV1(param: HashMap<String, String>, name: String): Boolean {
        return AlipaySignature.rsaCheckV1(
            param,
            configuration.alipayPublicKey,
            StandardCharsets.UTF_8.name(),
            configuration.signType
        )
    }
}

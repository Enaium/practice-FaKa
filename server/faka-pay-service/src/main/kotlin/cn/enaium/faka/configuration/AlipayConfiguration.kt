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

package cn.enaium.faka.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

/**
 * 支付宝相关配置
 *
 * @author Enaium
 */
@ConfigurationProperties(prefix = "alipay")
data class AlipayConfiguration
@ConstructorBinding constructor(
    /**
     * 应用公钥
     */
    val publicKey: String,

    /**
     * 应用私钥
     */
    val privateKey: String,

    /**
     * 支付宝公钥
     */
    val alipayPublicKey: String,

    /**
     * 应用ID
     */
    val appId: String,

    /**
     * 支付宝网关地址
     */
    val gateway: String,

    /**
     * 签名类型
     */
    val signType: String,

    /**
     * 方法 alipay.trade.page.pay
     *
     * 通知地址
     *
     * 因为可能不会收到,所以不能将通知状态为唯一凭证,只能调用调用查询接口了,所以通知地址可以不填
     */
    val notifyUrl: String,

    /**
     * 方法 alipay.trade.page.pay
     * 返回地址
     */
    val returnUrl: String,
)
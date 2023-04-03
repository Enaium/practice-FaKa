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

package cn.enaium.faka.result

/**
 * @author Enaium
 */
data class Result<T>(val code: Int, val message: String, val metadata: T) {

    enum class Status(val code: Int, val message: String) {
        SUCCESS(200, "成功"),
        FAIL(999, "失败"),
        PARAMETER_MISSING(1001, "参数缺失"),
        PASSWORD_NOT_SAME(2001, "密码不同"),
        CATEGORY_DOESNT_EXIST(2002, "分类不存在"),
        CATEGORY_ISNT_EMPTY(2003, "分类不为空"),
        NOT_LOGIN(2004, "未登录"),
        GOODS_DOESNT_EXIST(2005, "商品不存在"),
        HAS_KAMI(2006, "已有卡密"),
        STORE_DOESNT_EXIST(2007, "店铺不存在"),
        HAS_STORE(2008, "已有店铺"),
        GOODS_SOLD_OUT(2009, "商品售罄"),
        ORDER_DOESNT_EXIST(2010, "订单不存在"),
        ORDER_CREATED(2011, "订单以创建"),
        ORDER_UNPAID(2012, "订单未支付"),
        STORE_CLOSING(2013, "店铺打烊"),
        GOODS_ISNT_EMPTY(2014, "商品不为空"),
        BALANCE_IS_INSUFFICIENT(2015, "余额不足"),
        NO_PERMISSION(3001, "无权限")
    }

    object Builder {
        fun <T> success(
            status: Status = Status.SUCCESS,
            code: Int = status.code,
            message: String = status.message,
            metadata: T? = null
        ): Result<T?> {
            return Result(code, message, metadata)
        }

        fun <T> fail(
            status: Status = Status.FAIL,
            code: Int = status.code,
            message: String = status.message,
            metadata: T? = null
        ): Result<T?> {
            return Result(code, message, metadata)
        }
    }
}
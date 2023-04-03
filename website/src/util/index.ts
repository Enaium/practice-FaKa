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

import {ContactType, GoodsType, KamiStatusType, OrderStatusType, SaleOrderType} from "@/util/model";

export const contactTypeParse = (type: ContactType | undefined): string => {
    switch (type) {
        case ContactType.TENCENT_QQ:
            return "QQ"
        case ContactType.TENCENT_QQ_GROUP:
            return "QQ群"
        case ContactType.WECHAT:
            return "微信"
        case ContactType.PHONE:
            return "手机"
        case ContactType.EMAIL:
            return "邮箱"
        case ContactType.CUSTOM:
            return "自定义"
        default:
            return "不显示"
    }
}

export const saleOrderTypeParse = (type: SaleOrderType | undefined): string => {
    switch (type) {
        case SaleOrderType.NEW:
            return "先卖新卡"
        default:
            return "先卖旧卡";
    }
}

export const goodsTypeParse = (type: GoodsType | undefined): string => {
    switch (type) {
        case GoodsType.SINGLE:
            return "只需添加一个"
        default :
            return "添加多个卡密"
    }
}

export const kamiStatusTypeParse = (type: KamiStatusType | undefined): string => {
    switch (type) {
        case KamiStatusType.SELL:
            return "在售"
        case KamiStatusType.SOLD:
            return "已售"
        default:
            return "锁定"
    }
}

export const orderStatusTypeParse = (type: OrderStatusType | undefined): string => {
    switch (type) {
        case OrderStatusType.CLOSED:
            return "已关闭"
        case OrderStatusType.FINISHED:
            return "已完成"
        case OrderStatusType.SUCCESS:
            return "已成功"
        case OrderStatusType.WAIT_PAY:
            return "未支付"
        default:
            return "未创建"
    }
}

export const buildParameter = (data: any) => {
    const params: string[] = []
    Object.keys(data).forEach((key) => {
        let value = data[key]
        if (value !== undefined && value !== "") {
            params.push([key, encodeURIComponent(value)].join('='))
        }
    })

    if (params.length > 0) {
        return `?${params.join("&")}`
    } else {
        return ""
    }
}
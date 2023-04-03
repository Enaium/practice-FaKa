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

export interface IResult<T> {
    code: number
    message: string,
    metadata: T
}

export interface IPage<T> {
    content: T[]
    totalPages: number
    totalElements: number
    size: number
    number: number
}

export interface IUser {
    id: string
    username: string
    info: IUserInfo
    wallet: IUserWallet
    store: IStore
}

export interface IUserInfo {
    userId?: string
    avatarUrl?: string
    phoneNumber?: number
    wechatNumber?: string
    email?: string
    contactType?: ContactType
    contact?: string
    name?: string
    website?: string
    bulletin?: string
    payTip?: string,
    realName?: string
    alipayNumber?: string
}

export interface IUserWallet {
    userId?: string
    balance?: number
}

export interface ICategory {
    id?: string
    userId?: string
    goods?: ICategory[]
    name?: string
    priority?: number
    visible?: boolean
    createTime?: string
    updateTime?: string
}

export interface IGoods {
    id?: string
    userId?: string
    categoryId?: string
    category?: ICategory
    name?: string
    priority?: number
    price?: number
    description?: string
    type?: GoodsType
    saleOrder?: SaleOrderType
    stock?: number
    createTime?: string
    updateTime?: string
}

export interface IKami {
    id?: string
    goodsId?: string
    goods?: IGoods
    userId?: string
    content?: string
    status?: KamiStatusType
    createTime?: string
    updateTime?: string
}

export interface IStore {
    id?: string
    userId?: string
    user?: IUser
    name?: string
    path?: string
    closing?: boolean
    createTime?: string
    updateTime?: string
}

export interface IOrder {
    id?: string
    goodsId?: string
    goods?: IGoods
    userId?: string
    user: IUser
    kamis?: IKami[]
    merchant: string
    status: OrderStatusType
    payType?: PayType
    amount?: number
    createTime?: string
    updateTime?: string
}

export enum ContactType {
    /**
     * 腾讯QQ
     */
    TENCENT_QQ = "TENCENT_QQ",

    /**
     * 腾讯QQ群
     */
    TENCENT_QQ_GROUP = "TENCENT_QQ_GROUP",

    /**
     * 微信
     */
    WECHAT = "WECHAT",

    /**
     * 手机
     */
    PHONE = "PHONE",

    /**
     * 邮箱
     */
    EMAIL = "EMAIL",

    /**
     * 自定义
     */
    CUSTOM = "CUSTOM",

    /**
     * 无
     */
    NONE = "NONE"
}

export enum SaleOrderType {
    NEW = "NEW",
    OLD = "OLD"
}

export enum GoodsType {
    SINGLE = "SINGLE",
    MULTI = "MULTI"
}

export enum KamiStatusType {
    SELL = "SELL",
    SOLD = "SOLE",
    LOCK = "LOCK"
}

export enum OrderStatusType {
    CLOSED = "CLOSED",
    FINISHED = "FINISHED",
    SUCCESS = "SUCCESS",
    WAIT_PAY = "WAIT_PAY",
    WAIT_CREATE = "WAIT_CREATE"
}

export enum PayType {
    ALIPAY = "ALIPAY",
    WECHAT = "WECHAT"
}
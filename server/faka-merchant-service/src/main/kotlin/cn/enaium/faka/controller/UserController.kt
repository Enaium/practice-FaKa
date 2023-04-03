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

import cn.dev33.satoken.stp.StpUtil
import cn.enaium.faka.model.entity.*
import cn.enaium.faka.model.fetch.userFetch
import cn.enaium.faka.model.post.UserPost
import cn.enaium.faka.model.type.OrderStatusType
import cn.enaium.faka.repository.OrderRepository
import cn.enaium.faka.repository.UserInfoRepository
import cn.enaium.faka.repository.UserRepository
import cn.enaium.faka.repository.WalletRecordRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.util.PermissionUtil
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.count
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.sum
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@RestController
@Transactional
@RequestMapping("/user")
class UserController(
    val userRepository: UserRepository,
    val userInfoRepository: UserInfoRepository,
    val orderRepository: OrderRepository,
    val walletRecordRepository: WalletRecordRepository,
    val sql: KSqlClient
) {
    /**
     * 获取用户
     *
     * @param id 用户ID
     */
    @GetMapping("/{id}")
    fun user(@PathVariable id: UUID): Result<User?> {
        return Result.Builder.success(
            metadata = userRepository.findNullable(id, userFetch(PermissionUtil.isLogin()))
        )
    }

    /**
     * 获取所有订单
     *
     * @param id 用户ID
     * @param page 第几页
     * @param size 每页有几个
     * @return
     */
    @GetMapping("/{id}/orders")
    fun order(
        @PathVariable id: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): Result<Page<Order>?> {
        return Result.Builder.success(
            metadata = orderRepository.pager(PageRequest.of(page, size)).execute(sql.createQuery(Order::class) {
                where(table.userId eq id)
                select(table.fetchBy {
                    allScalarFields()
                    goods {
                        allScalarFields()
                    }
                    kamis {
                        allScalarFields()
                    }
                })
            })
        )
    }

    /**
     * 总收益
     *
     * @param id 用户ID
     */
    @GetMapping("/{id}/orders/income")
    fun income(@PathVariable id: UUID): Result<BigDecimal?> {
        return Result.Builder.success(metadata = sql.createQuery(Order::class) {
            where(table.userId eq id, table.status eq OrderStatusType.SUCCESS)
            select(sum(table.amount))
        }.fetchOneOrNull())
    }

    /**
     * 销售
     *
     * @param id 用户ID
     */
    @GetMapping("/{id}/orders/volume")
    fun volume(@PathVariable id: UUID): Result<Long?> {
        return Result.Builder.success(
            metadata = sql.createQuery(Order::class) {
                where(table.status eq OrderStatusType.SUCCESS,
                    table.asTableEx().kamis.id valueIn subQuery(Kami::class) {
                        select(table.id)
                    })
                select(count(table.id))
            }.fetchOneOrNull()
        )
    }

    @GetMapping("/{id}/wallet/balance")
    fun balance(@PathVariable id: UUID): Result<BigDecimal?> {
        return Result.Builder.success(metadata = walletRecordRepository.getBalanceByUserId(id) ?: BigDecimal.ZERO)
    }

    @PostMapping("/{id}/wallet/withdrawal")
    fun withdrawal(@PathVariable id: UUID): Result<Nothing?> {
        val balance = (walletRecordRepository.getBalanceByUserId(id)
            ?: return Result.Builder.fail(status = Result.Status.BALANCE_IS_INSUFFICIENT))

        if (balance <= BigDecimal.ZERO) {
            return Result.Builder.fail(status = Result.Status.BALANCE_IS_INSUFFICIENT)
        }

        walletRecordRepository.insert(new(WalletRecord::class).by {
            userId = UUID.fromString(StpUtil.getLoginIdAsString())
            amount = balance.negate()
            createTime = LocalDateTime.now()
        })
        return Result.Builder.success()
    }

    /**
     * 更新用户信息
     *
     * @param id 用户ID
     * @param param 用户信息
     */
    @PatchMapping("/{id}")
    fun patch(@PathVariable id: UUID, @RequestBody param: UserPost): Result<Nothing?> {
        userInfoRepository.update(new(UserInfo::class).by {
            userId = id
            param.avatarUrl?.let { avatarUrl = it }
            param.phoneNumber?.let { phoneNumber = it }
            param.wechatNumber?.let { wechatNumber = it }
            param.email?.let { email = it }
            param.contactType?.let { contactType = it }
            param.contact?.let { contact = it }
            param.name?.let { name = it }
            param.website?.let { website = it }
            param.payTip?.let { payTip = it }
            param.bulletin?.let { bulletin = it }
        })
        return Result.Builder.success()
    }
}
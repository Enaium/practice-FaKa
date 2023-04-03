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
import cn.enaium.faka.model.post.GoodsPost
import cn.enaium.faka.repository.CategoryRepository
import cn.enaium.faka.repository.GoodsRepository
import cn.enaium.faka.repository.KamiRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.util.PermissionUtil
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.like
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@Validated
@RestController
@RequestMapping("/goods")
class GoodsController(
    val categoryRepository: CategoryRepository,
    val goodsRepository: GoodsRepository,
    val kamiRepository: KamiRepository,
    val sql: KSqlClient
) {

    /**
     * 添加商品
     *
     * @param category 分类ID
     * @param user 用户ID
     * @param param 商品参数
     */
    @PostMapping
    fun goods(
        @RequestParam category: UUID,
        @RequestParam user: UUID,
        @RequestBody param: GoodsPost
    ): Result<Nothing?> {

        if (!PermissionUtil.checkLogin(user)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        val findNullable = categoryRepository.findNullable(category)
            ?: return Result.Builder.fail(status = Result.Status.CATEGORY_DOESNT_EXIST)

        if (!PermissionUtil.checkLogin(findNullable.userId)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        goodsRepository.insert(new(Goods::class).by {
            userId = user
            categoryId = category
            name = param.name
            saleOrder = param.saleOrder
            priority = param.priority
            price = param.price
            type = param.type
            description = param.description
            createTime = LocalDateTime.now()
            updateTime = LocalDateTime.now()
        })

        return Result.Builder.success()
    }

    /**
     * 更新商品信息
     *
     * @param id 商品ID
     * @param param 商品参数
     */
    @PatchMapping("/{id}")
    fun patch(@PathVariable id: UUID, @RequestBody param: GoodsPost): Result<Nothing?> {
        val findNullable =
            goodsRepository.findNullable(id) ?: return Result.Builder.fail(status = Result.Status.GOODS_DOESNT_EXIST)

        if (!PermissionUtil.checkLogin(findNullable.userId)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        goodsRepository.update(new(Goods::class).by {
            this.id = id
            name = param.name
            saleOrder = param.saleOrder
            priority = param.priority
            price = param.price
            type = param.type
            description = param.description
            updateTime = LocalDateTime.now()
        })

        return Result.Builder.success()
    }

    /**
     * 获取商品信息
     *
     * @param id 商品ID
     */
    @GetMapping("/{id}")
    fun goods(@PathVariable id: UUID): Result<Goods?> {
        return Result.Builder.success(metadata = goodsRepository.findNullable(id, newFetcher(Goods::class).by {
            allScalarFields()
            stock()
        }))
    }

    /**
     * 获取所有商品
     *
     * @param page 第几页
     * @param size 每页有几个
     * @param user 用户ID
     * @param keyword 关键字
     */
    @GetMapping
    fun goods(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam user: UUID,
        @RequestParam category: UUID?,
        @RequestParam keyword: String?,
        @RequestParam order: String?
    ): Result<Page<Goods>?> {
        return Result.Builder.success(
            metadata = goodsRepository.pager(PageRequest.of(page, size)).execute(sql.createQuery(Goods::class) {
                where(table.userId eq user)
                category?.let {
                    where(table.category.id eq category)
                }

                keyword?.let {
                    where(
                        or(
                            table.name like keyword,
                            table.category.name like keyword
                        )
                    )
                }

                order?.let {
                    when (order) {
                        "name" -> {
                            orderBy(table.name)
                        }

                        "priority" -> {
                            orderBy(table.priority)
                        }

                        "price" -> {
                            orderBy(table.price)
                        }

                        "createTime" -> {
                            orderBy(table.createTime)
                        }

                        "updateTime" -> {
                            orderBy(table.updateTime)
                        }
                    }
                }

                select(table.fetchBy {
                    allScalarFields()
                    category {
                        name()
                    }
                    stock()
                })
            })
        )
    }

    /**
     * 删除商品
     *
     * @param id 商品ID
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Result<Nothing?> {
        if (!goodsRepository.existsById(id)) {
            return Result.Builder.fail(status = Result.Status.GOODS_DOESNT_EXIST)
        }

        if (kamiRepository.existsByGoodsId(id)) {
            return Result.Builder.fail(status = Result.Status.GOODS_ISNT_EMPTY)
        }

        goodsRepository.deleteById(id)
        return Result.Builder.success()
    }
}
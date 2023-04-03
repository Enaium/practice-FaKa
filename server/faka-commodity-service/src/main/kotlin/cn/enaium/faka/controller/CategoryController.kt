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
import cn.enaium.faka.model.post.CategoryPost
import cn.enaium.faka.repository.CategoryRepository
import cn.enaium.faka.repository.GoodsRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.util.PermissionUtil
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.like
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
@RequestMapping("/category")
class CategoryController(
    val sql: KSqlClient,
    val categoryRepository: CategoryRepository,
    val goodsRepository: GoodsRepository
) {
    /**
     * 添加分类
     *
     * @param user 用户ID
     * @param param 分类信息
     */
    @PostMapping
    fun category(@RequestParam user: UUID, @RequestBody param: CategoryPost): Result<Nothing?> {
        if (!PermissionUtil.checkLogin(user)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        categoryRepository.insert(new(Category::class).by {
            userId = user
            name = param.name
            priority = param.priority
            createTime = LocalDateTime.now()
            updateTime = LocalDateTime.now()
        })
        return Result.Builder.success()
    }

    /**
     * 更新分类
     *
     * @param id 分类ID
     * @param param 分类参数
     */
    @PatchMapping("/{id}")
    fun patch(@PathVariable id: UUID, @RequestBody param: CategoryPost): Result<Nothing?> {
        val findNullable = categoryRepository.findNullable(id)
            ?: return Result.Builder.fail(status = Result.Status.CATEGORY_DOESNT_EXIST)

        if (!PermissionUtil.checkLogin(findNullable.userId)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        categoryRepository.update(new(Category::class).by {
            this.id = id
            name = param.name
            priority = param.priority
            updateTime = LocalDateTime.now()
        })

        return Result.Builder.success()
    }


    /**
     * 获取所有分类
     *
     * @param page 第几页
     * @param size 每页有几个
     * @param user 用户ID
     * @param keyword 关键字
     * @param order 排序字段
     */
    @GetMapping
    fun category(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam user: UUID,
        @RequestParam keyword: String?,
        @RequestParam order: String?
    ): Result<Page<Category>?> {
        return Result.Builder.success(
            metadata = categoryRepository.pager(PageRequest.of(page, size))
                .execute(sql.createQuery(Category::class) {
                    where(table.userId eq user)

                    keyword?.let {
                        where(table.name like keyword)
                    }

                    order?.let {
                        when (order) {
                            "name" -> {
                                orderBy(table.name)
                            }

                            "priority" -> {
                                orderBy(table.priority)
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
                        goods()
                    })
                })
        )
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): Result<Nothing?> {
        if (!categoryRepository.existsById(id)) {
            return Result.Builder.fail(status = Result.Status.CATEGORY_DOESNT_EXIST)
        }

        if (goodsRepository.existsByCategoryId(id)) {
            return Result.Builder.fail(status = Result.Status.CATEGORY_ISNT_EMPTY)
        }

        categoryRepository.deleteById(id)
        return Result.Builder.success()
    }
}
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
import cn.enaium.faka.model.post.KamiPost
import cn.enaium.faka.model.type.GoodsType
import cn.enaium.faka.repository.GoodsRepository
import cn.enaium.faka.repository.KamiRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.util.PermissionUtil
import org.babyfish.jimmer.kt.new
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.like
import org.babyfish.jimmer.sql.kt.ast.expression.or
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@Transactional
@RestController
@RequestMapping("/kami")
class KamiController(
    val kamiRepository: KamiRepository,
    val goodsRepository: GoodsRepository,
    val sql: KSqlClient
) {
    /**
     * 添加卡密
     *
     * @param goods 商品ID
     * @param param 参数
     */
    @PostMapping
    fun kami(@RequestParam goods: UUID, @RequestParam user: UUID, @RequestBody param: KamiPost): Result<Nothing?> {

        val findNullable =
            goodsRepository.findNullable(goods) ?: return Result.Builder.fail(status = Result.Status.GOODS_DOESNT_EXIST)

        if (!PermissionUtil.checkLogin(findNullable.userId)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        val findByGoodsId = kamiRepository.findByGoodsId(goods)
        findByGoodsId?.let {
            if (findNullable.type == GoodsType.SINGLE) {
                return Result.Builder.fail(status = Result.Status.HAS_KAMI)
            }
        }

        if (findNullable.type == GoodsType.MULTI) {
            param.content.split("\n").forEach { kami ->
                if (kami.isBlank()) {
                    return@forEach
                }
                kamiRepository.insert(new(Kami::class).by {
                    goodsId = goods
                    userId = user
                    content = kami
                    createTime = LocalDateTime.now()
                    updateTime = LocalDateTime.now()
                })
            }
        } else {
            kamiRepository.insert(new(Kami::class).by {
                goodsId = goods
                userId = user
                content = param.content
                createTime = LocalDateTime.now()
                updateTime = LocalDateTime.now()
            })
        }

        return Result.Builder.success()
    }

    /**
     * 获取所有卡密
     *
     * @param page 第几页
     * @param size 每页有几个
     * @param user 用户ID
     * @param keyword 关键字
     */
    @GetMapping
    fun kami(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam user: UUID,
        @RequestParam keyword: String?,
    ): Result<Page<Kami>?> {
        return Result.Builder.success(
            metadata = kamiRepository.pager(PageRequest.of(page, size)).execute(sql.createQuery(Kami::class) {
                where(table.userId eq user,
                    keyword?.let {
                        or(
                            table.content like keyword,
                            table.goods.name like keyword,
                            table.goods.category.name like keyword
                        )
                    })
                select(table.fetchBy {
                    allScalarFields()
                    goods {
                        name()
                        category {
                            name()
                        }
                    }
                })
            })
        )
    }
}
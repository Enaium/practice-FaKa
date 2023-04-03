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

import cn.enaium.faka.model.entity.Store
import cn.enaium.faka.model.entity.by
import cn.enaium.faka.model.post.StorePost
import cn.enaium.faka.repository.StoreRepository
import cn.enaium.faka.result.Result
import cn.enaium.faka.util.PermissionUtil
import org.babyfish.jimmer.kt.new
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime
import java.util.*

/**
 * @author Enaium
 */
@RestController
@RequestMapping("/store")
class StoreController(val storeRepository: StoreRepository) {
    /**
     * 创建店铺
     *
     * @param user 用户ID
     * @param param 参数
     */
    @PostMapping
    fun store(@RequestParam user: UUID, @RequestBody param: StorePost): Result<Nothing?> {
        if (!PermissionUtil.checkLogin(user)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        storeRepository.findByUserId(user)?.let {
            return Result.Builder.fail(status = Result.Status.HAS_STORE)
        }

        storeRepository.insert(new(Store::class).by {
            userId = user
            name = param.name
            path = param.path
            closing = param.closing
            createTime = LocalDateTime.now()
            updateTime = LocalDateTime.now()
        })

        return Result.Builder.success()
    }

    /**
     * 修改店铺信息
     *
     * @param id 店铺ID
     * @param param 参数
     */
    @PatchMapping("/{id}")
    fun patch(@PathVariable id: UUID, @RequestBody param: StorePost): Result<Nothing?> {
        val findNullable =
            storeRepository.findNullable(id) ?: return Result.Builder.fail(status = Result.Status.STORE_DOESNT_EXIST)
        if (!PermissionUtil.checkLogin(findNullable.userId)) {
            return Result.Builder.fail(status = Result.Status.NO_PERMISSION)
        }

        storeRepository.update(new(Store::class).by {
            this.id = id
            name = param.name
            path = param.path
            closing = param.closing
            updateTime = LocalDateTime.now()
        })
        return Result.Builder.success()
    }

    /**
     * 获取店铺信息
     *
     * @param user 用户
     * @param path 地址
     */
    @GetMapping
    fun store(
        @RequestParam user: UUID?,
        @RequestParam path: String?
    ): Result<Store?> {
        user?.let {
            return Result.Builder.success(metadata = storeRepository.findByUserId(user))
        }
        path?.let {
            return Result.Builder.success(metadata = storeRepository.findByPath(path))
        }
        return Result.Builder.fail()
    }

    /**
     * 获取店铺信息
     *
     * @param id 店铺ID
     */
    @GetMapping("/{id}")
    fun store(@PathVariable id: UUID): Result<Store?> {
        val findNullable =
            storeRepository.findNullable(id) ?: return Result.Builder.fail(Result.Status.STORE_DOESNT_EXIST)
        return Result.Builder.success(metadata = findNullable)
    }
}
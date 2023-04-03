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

package cn.enaium.faka.model.fetch

import cn.enaium.faka.model.entity.User
import cn.enaium.faka.model.entity.by
import org.babyfish.jimmer.sql.fetcher.Fetcher
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher

/**
 * @author Enaium
 */
fun userFetch(permission: Boolean): Fetcher<User> {
    return if (permission) {
        newFetcher(User::class).by {
            allScalarFields()
            info {
                allScalarFields()
            }
            wallet {
                allScalarFields()
            }
            store {
                allScalarFields()
            }
        }
    } else {
        newFetcher(User::class).by {
            allScalarFields()
            password(false)
            info {
                allScalarFields()
                wechatNumber(false)
                alipayNumber(false)
                realName(false)
            }
            wallet(false)
            store {
                allScalarFields()
            }
        }
    }
}
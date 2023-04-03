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

package cn.enaium.faka.util

import org.springframework.util.DigestUtils

/**
 * @author Enaium
 */
object DigestUtil {
    fun md5(text: String): String {
        return DigestUtils.md5DigestAsHex((text + "ABA-ABA").toByteArray(Charsets.UTF_8))
    }
}
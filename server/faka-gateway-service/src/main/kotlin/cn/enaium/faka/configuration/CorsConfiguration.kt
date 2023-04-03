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

package cn.enaium.faka.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.web.server.WebFilter

@Configuration
class CorsConfiguration {
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    fun corsFilter(): WebFilter {
        return WebFilter { exchange, chain ->
            with(exchange.response.headers) {
                this[HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN] = "*"
                this[HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS] = "*"
                this[HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS] = "*"
                this[HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS] = "true"
                this[HttpHeaders.ACCESS_CONTROL_MAX_AGE] = "3600"
            }
            chain.filter(exchange)
        }
    }
}

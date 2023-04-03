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

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER
import io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringDocConfiguration(
    @Value("\${spring.application.name}") val name: String,
    @Value("\${website}") val website: String
) {
    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI().info(Info().title(name).version("1.0.0"))
            .addServersItem(Server().url("$website/api/${name.substring(0, name.indexOf("-service"))}")).components(
                Components().addSecuritySchemes(
                    "apiKeyScheme", SecurityScheme()
                        .type(APIKEY)
                        .`in`(HEADER)
                        .name("token")
                )
            ).addSecurityItem(SecurityRequirement().addList("apiKeyScheme"))
}

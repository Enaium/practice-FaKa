package cn.enaium.faka.configuration

import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties
import org.springdoc.core.properties.SwaggerUiConfigProperties
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteDefinition
import org.springframework.cloud.gateway.route.RouteDefinitionLocator
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled

@Configuration
@EnableScheduling
class SwaggerConfiguration(
    val configProperties: SwaggerUiConfigProperties,
    val routeLocator: RouteDefinitionLocator,
    @Value("\${spring.application.name}") val name: String
) {
    @Scheduled(fixedDelay = 5)
    fun apis() {
        configProperties.urls = routeLocator.routeDefinitions.collectList().block()!!.filter { route: RouteDefinition ->
            route.uri.host != null && route.uri.host != name
        }.distinct().map { route: RouteDefinition ->
            AbstractSwaggerUiConfigProperties.SwaggerUrl().apply {
                name = route.uri.host
                url = "/api/${name.substring(0, name.indexOf("-service"))}/v3/api-docs"
            }
        }.toSet()
    }
}

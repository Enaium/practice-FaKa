package cn.enaium.faka.configuration

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Enaium
 */
@Configuration
class RouteConfiguration {
    @Bean
    fun routerLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes()
            .route("commodity-service") { r: PredicateSpec ->
                r.path("/api/commodity/**").filters { it.stripPrefix(2) }.uri("lb://commodity-service")
            }
            .route("merchant-service") { r: PredicateSpec ->
                r.path("/api/merchant/**").filters { it.stripPrefix(2) }.uri("lb://merchant-service")
            }
            .route("pay-service") { r: PredicateSpec ->
                r.path("/api/pay/**").filters { it.stripPrefix(2) }.uri("lb://pay-service")
            }
            .build()
    }
}
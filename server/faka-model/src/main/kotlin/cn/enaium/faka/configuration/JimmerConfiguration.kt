package cn.enaium.faka.configuration

import cn.enaium.faka.model.entity.ENTITY_MANAGER
import org.babyfish.jimmer.sql.runtime.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Enaium
 */
@Configuration
class JimmerConfiguration {
    @Bean
    fun entityManager(): EntityManager = ENTITY_MANAGER
}
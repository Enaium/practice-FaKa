package cn.enaium.faka

import cn.enaium.faka.configuration.UnmatchedConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

/**
 * @author Enaium
 */
@EnableConfigurationProperties(UnmatchedConfiguration::class)
@SpringBootApplication
class FakaGatewayService

fun main(args: Array<String>) {
    runApplication<FakaGatewayService>(*args)
}
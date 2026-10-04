package com.sample.hexagonal.common.config

import io.micrometer.core.instrument.MeterRegistry
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.boot.autoconfigure.AutoConfiguration
import java.net.InetAddress
import java.net.UnknownHostException

@AutoConfiguration
class CustomMetricConfig {

    private val logger = KotlinLogging.logger { }

    @Bean
    fun metricsCommonTags(@Value("\${spring.application.name}") applicationName: String): MeterRegistryCustomizer<MeterRegistry> =
        MeterRegistryCustomizer<MeterRegistry> { registry: MeterRegistry ->
            registry.config().commonTags("application", applicationName)
            try {
                registry.config().commonTags("host", InetAddress.getLocalHost().hostName)
            } catch (uhe: UnknownHostException) {
                logger.error(uhe) { "Error getting hostname" }
            }
        }
}

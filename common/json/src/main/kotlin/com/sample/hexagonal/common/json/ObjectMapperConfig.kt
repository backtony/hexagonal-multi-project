package com.sample.hexagonal.common.json

import tools.jackson.databind.json.JsonMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ObjectMapperConfig {

    @Primary
    @Bean
    fun objectMapper(): JsonMapper = ObjectMapperFactory.create()
}

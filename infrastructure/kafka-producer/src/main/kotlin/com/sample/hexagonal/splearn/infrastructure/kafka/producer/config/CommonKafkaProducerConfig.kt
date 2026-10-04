package com.sample.hexagonal.splearn.infrastructure.kafka.producer.config

import com.sample.hexagonal.common.json.ObjectMapperFactory
import com.sample.hexagonal.splearn.infrastructure.kafka.producer.handler.CommonKafkaListener
import io.micrometer.core.instrument.MeterRegistry
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.kafka.autoconfigure.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.MicrometerProducerListener
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JacksonJsonSerializer

@Configuration
class CommonKafkaProducerConfig(
    private val kafkaProperties: KafkaProperties,
    private val meterRegistry: MeterRegistry,
) {

    @Bean
    fun commonKafkaTemplate(): KafkaTemplate<String, Any> = KafkaTemplate(commonProducerFactory()).apply {
        setProducerListener(CommonKafkaListener())
    }

    @Bean
    fun commonProducerFactory(): ProducerFactory<String, Any> {
        val keySerializer = StringSerializer()
        val valueSerializer = JacksonJsonSerializer<Any>(ObjectMapperFactory.create())

        return DefaultKafkaProducerFactory(kafkaProperties.buildProducerProperties(), keySerializer, valueSerializer)
            .apply { addListener(MicrometerProducerListener(meterRegistry)) }
    }
}

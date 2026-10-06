package com.sample.hexagonal.sample.infrastructure.kafka.producer.config

import com.sample.hexagonal.common.json.ObjectMapperFactory
import com.sample.hexagonal.sample.infrastructure.kafka.producer.handler.CommonKafkaListener
import io.micrometer.core.instrument.MeterRegistry
import org.apache.kafka.clients.producer.ProducerConfig
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

    @Bean
    fun anyKafkaTemplate(): KafkaTemplate<Any, Any> {
        val properties = kafkaProperties.buildProducerProperties()
            .apply { put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java) }
        val producerFactory = DefaultKafkaProducerFactory<Any, Any>(properties, null, JacksonJsonSerializer(ObjectMapperFactory.create()))
            .apply { addListener(MicrometerProducerListener(meterRegistry)) }

        return KafkaTemplate(producerFactory)
    }
}

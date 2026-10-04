package com.sample.hexagonal.splearn.adapter.outbound.producer.config

import com.sample.hexagonal.splearn.application.support.message.InternalTopic
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import io.namastack.outbox.kafka.KafkaOutboxRouting
import io.namastack.outbox.kafka.kafkaOutboxRouting
import io.namastack.outbox.routing.selector.OutboxPayloadSelector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NamastackConfig {
    @Bean
    fun kafkaOutboxRouting(): KafkaOutboxRouting = kafkaOutboxRouting {
        route(OutboxPayloadSelector.type(DomainEvent::class.java)) {
            target { payload, _ -> InternalTopic.from(payload as DomainEvent) }
        }
    }
}

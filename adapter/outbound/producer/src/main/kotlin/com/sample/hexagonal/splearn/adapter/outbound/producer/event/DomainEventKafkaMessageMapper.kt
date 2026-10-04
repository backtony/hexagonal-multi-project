package com.sample.hexagonal.splearn.adapter.outbound.producer.event

import com.sample.hexagonal.splearn.application.support.message.InternalTopic
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import com.sample.hexagonal.splearn.infrastructure.kafka.producer.KafkaMessage

object DomainEventKafkaMessageMapper {
    fun mapToKafkaMessage(domainEvent: DomainEvent): KafkaMessage = KafkaMessage(
        topic = InternalTopic.from(domainEvent),
        key = domainEvent.key,
        data = domainEvent,
        type = domainEvent.javaClass.name,
        registeredDate = domainEvent.registeredDate,
    )
}

package com.sample.hexagonal.splearn.adapter.outbound.producer.event

import com.sample.hexagonal.splearn.application.support.event.DomainEventPublisher
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class TransactionalDomainEventProducer(private val applicationEventPublisher: ApplicationEventPublisher) : DomainEventPublisher {
    override fun publishEvent(domainEvent: DomainEvent) {
        applicationEventPublisher.publishEvent(DomainEventKafkaMessageMapper.mapToKafkaMessage(domainEvent))
    }
}

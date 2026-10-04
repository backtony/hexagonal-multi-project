package com.sample.hexagonal.splearn.adapter.outbound.producer.event

import com.sample.hexagonal.splearn.application.support.event.DomainEventPublisher
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import io.namastack.outbox.Outbox
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class DefaultDomainEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val outbox: Outbox,
) : DomainEventPublisher {
    override fun publishInternalEvent(domainEvent: DomainEvent) {
        applicationEventPublisher.publishEvent(domainEvent)
    }

    override fun publishExternalEvent(domainEvent: DomainEvent) {
        outbox.schedule(domainEvent, domainEvent.key)
    }
}

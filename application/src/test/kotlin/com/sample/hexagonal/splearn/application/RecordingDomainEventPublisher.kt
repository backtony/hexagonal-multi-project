package com.sample.hexagonal.splearn.application

import com.sample.hexagonal.splearn.application.support.event.DomainEventPublisher
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.ApplicationEventPublisher

@TestComponent
class RecordingDomainEventPublisher(private val applicationEventPublisher: ApplicationEventPublisher) : DomainEventPublisher {
    val internalEvents = mutableListOf<DomainEvent>()
    val externalEvents = mutableListOf<DomainEvent>()

    override fun publishInternalEvent(domainEvent: DomainEvent) {
        internalEvents.add(domainEvent)
        applicationEventPublisher.publishEvent(domainEvent)
    }

    override fun publishExternalEvent(domainEvent: DomainEvent) {
        externalEvents.add(domainEvent)
    }
}

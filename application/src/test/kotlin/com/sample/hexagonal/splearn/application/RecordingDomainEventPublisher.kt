package com.sample.hexagonal.splearn.application

import com.sample.hexagonal.splearn.application.support.event.DomainEventPublisher
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import org.springframework.boot.test.context.TestComponent

@TestComponent
class RecordingDomainEventPublisher : DomainEventPublisher {
    val events = mutableListOf<DomainEvent>()

    override fun publishEvent(domainEvent: DomainEvent) {
        events.add(domainEvent)
    }
}

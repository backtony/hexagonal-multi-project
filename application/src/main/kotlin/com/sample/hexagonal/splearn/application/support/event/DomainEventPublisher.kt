package com.sample.hexagonal.splearn.application.support.event

import com.sample.hexagonal.splearn.domain.event.DomainEvent

fun interface DomainEventPublisher {
    fun publishEvent(domainEvent: DomainEvent)
}

package com.sample.hexagonal.splearn.domain.event

import com.sample.hexagonal.splearn.domain.AbstractEntity
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Transient

@MappedSuperclass
abstract class AbstractAggregateRoot : AbstractEntity() {
    @Transient
    private val domainEvents: MutableList<DomainEvent> = mutableListOf()

    protected fun registerEvent(event: DomainEvent) {
        domainEvents.add(event)
    }

    fun pullDomainEvents(): List<DomainEvent> = domainEvents.toList().also { domainEvents.clear() }
}

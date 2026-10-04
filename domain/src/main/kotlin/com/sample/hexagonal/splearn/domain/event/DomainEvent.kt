package com.sample.hexagonal.splearn.domain.event

import java.time.LocalDateTime

interface DomainEvent {
    val key: String
    val aggregateRoot: AggregateRoot
    val registeredDate: LocalDateTime
}

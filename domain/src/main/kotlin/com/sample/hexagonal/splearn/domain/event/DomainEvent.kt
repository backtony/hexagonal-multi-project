package com.sample.hexagonal.splearn.domain.event

import java.time.LocalDateTime

interface DomainEvent {
    val key: String
    val aggregateType: AggregateType
    val registeredDate: LocalDateTime
}

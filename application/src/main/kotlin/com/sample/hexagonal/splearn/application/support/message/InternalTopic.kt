package com.sample.hexagonal.splearn.application.support.message

import com.sample.hexagonal.splearn.domain.event.AggregateType
import com.sample.hexagonal.splearn.domain.event.DomainEvent

object InternalTopic {
    const val MEMBER = "MEMBER"
    const val INSTRUCTOR = "INSTRUCTOR"
    const val COURSE = "COURSE"
    const val CURRICULUM = "CURRICULUM"
    const val ENROLLMENT = "ENROLLMENT"

    fun from(domainEvent: DomainEvent): String = when (domainEvent.aggregateType) {
        AggregateType.MEMBER -> MEMBER
        AggregateType.INSTRUCTOR -> INSTRUCTOR
        AggregateType.COURSE -> COURSE
        AggregateType.CURRICULUM -> CURRICULUM
        AggregateType.ENROLLMENT -> ENROLLMENT
    }
}

package com.sample.hexagonal.splearn.application.support.message

import com.sample.hexagonal.splearn.domain.event.AggregateRoot
import com.sample.hexagonal.splearn.domain.event.DomainEvent

object InternalTopic {
    const val MEMBER = "MEMBER"
    const val INSTRUCTOR = "INSTRUCTOR"
    const val COURSE = "COURSE"
    const val CURRICULUM = "CURRICULUM"
    const val ENROLLMENT = "ENROLLMENT"

    fun from(domainEvent: DomainEvent): String = when (domainEvent.aggregateRoot) {
        AggregateRoot.MEMBER -> MEMBER
        AggregateRoot.INSTRUCTOR -> INSTRUCTOR
        AggregateRoot.COURSE -> COURSE
        AggregateRoot.CURRICULUM -> CURRICULUM
        AggregateRoot.ENROLLMENT -> ENROLLMENT
    }
}

package com.sample.hexagonal.splearn.domain.course.event

import com.sample.hexagonal.splearn.domain.event.AggregateType
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import java.time.LocalDateTime

data class CourseCreatedEvent(
    val courseId: Long,
    override val registeredDate: LocalDateTime = LocalDateTime.now(),
) : DomainEvent {
    override val key = courseId.toString()
    override val aggregateType = AggregateType.COURSE
}

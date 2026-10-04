package com.sample.hexagonal.splearn.domain.member.event

import com.sample.hexagonal.splearn.domain.event.AggregateType
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import java.time.LocalDateTime

data class MemberRegisteredEvent(
    val memberId: Long,
    val email: String,
    override val registeredDate: LocalDateTime = LocalDateTime.now(),
) : DomainEvent {
    override val key = memberId.toString()
    override val aggregateType = AggregateType.MEMBER
}

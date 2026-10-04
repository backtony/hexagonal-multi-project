package com.sample.hexagonal.splearn.domain.member.event

import com.sample.hexagonal.splearn.domain.event.AggregateRoot
import com.sample.hexagonal.splearn.domain.event.DomainEvent
import java.time.LocalDateTime

class MemberRegisteredEvent(
    val memberId: Long,
    override val registeredDate: LocalDateTime = LocalDateTime.now(),
) : DomainEvent {
    override val key = memberId.toString()
    override val aggregateRoot = AggregateRoot.MEMBER
}

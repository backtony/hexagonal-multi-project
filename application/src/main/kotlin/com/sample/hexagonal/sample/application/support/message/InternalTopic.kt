package com.sample.hexagonal.sample.application.support.message

import com.sample.hexagonal.sample.domain.event.AggregateRoot
import com.sample.hexagonal.sample.domain.event.DomainEvent

/**
 * 토픽 이름은 [AggregateRoot] 이름과 같다. `@KafkaListener(topics = ...)` 에서 쓰도록 애그리거트마다 상수를 추가한다.
 */
object InternalTopic {
    fun from(domainEvent: DomainEvent): String = domainEvent.aggregateRoot.name
}

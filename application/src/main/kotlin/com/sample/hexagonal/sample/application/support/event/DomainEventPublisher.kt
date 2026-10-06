package com.sample.hexagonal.sample.application.support.event

import com.sample.hexagonal.sample.domain.event.DomainEvent

interface DomainEventPublisher {
    /**
     * 같은 트랜잭션 안에서 내부 리스너가 처리한다. 리스너가 실패하면 함께 롤백된다.
     */
    fun publishInternalEvent(domainEvent: DomainEvent)

    /**
     * outbox 에 저장되어 커밋 후 Kafka 로 최소 한 번 전달된다. 중복 전달될 수 있으므로 consumer 는 멱등해야 한다.
     */
    fun publishExternalEvent(domainEvent: DomainEvent)
}

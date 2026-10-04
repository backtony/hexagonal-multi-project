package com.sample.hexagonal.splearn.adapter.outbound.producer.event

import com.sample.hexagonal.splearn.domain.member.event.MemberRegisteredEvent
import com.sample.hexagonal.splearn.infrastructure.kafka.producer.KafkaMessage
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

class TransactionalDomainEventProducerTest :
    StringSpec({
        "도메인 이벤트를 KafkaMessage 로 변환해 발행한다" {
            val publisher = mockk<ApplicationEventPublisher>(relaxed = true)
            val event = MemberRegisteredEvent(1L, "splearn@splearn.app")

            TransactionalDomainEventProducer(publisher).publishEvent(event)

            val message = slot<KafkaMessage>()
            verify { publisher.publishEvent(capture(message)) }
            message.captured.topic shouldBe "MEMBER"
            message.captured.key shouldBe "1"
            message.captured.type shouldBe MemberRegisteredEvent::class.java.name
            message.captured.data shouldBe event
        }
    })

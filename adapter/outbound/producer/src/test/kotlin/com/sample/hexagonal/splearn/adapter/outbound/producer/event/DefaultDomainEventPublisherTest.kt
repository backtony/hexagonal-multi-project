package com.sample.hexagonal.splearn.adapter.outbound.producer.event

import com.sample.hexagonal.splearn.domain.member.event.MemberRegisteredEvent
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk
import io.mockk.verify
import io.namastack.outbox.Outbox
import org.springframework.context.ApplicationEventPublisher

class DefaultDomainEventPublisherTest :
    StringSpec({
        val event = MemberRegisteredEvent(1L)

        "내부 이벤트는 애플리케이션 이벤트로 발행하고 outbox 에 저장하지 않는다" {
            val applicationEventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
            val outbox = mockk<Outbox>(relaxed = true)

            DefaultDomainEventPublisher(applicationEventPublisher, outbox).publishInternalEvent(event)

            verify { applicationEventPublisher.publishEvent(event as Any) }
            verify(exactly = 0) { outbox.schedule(any(), any<String>()) }
        }

        "외부 이벤트는 key 로 outbox 에 저장하고 애플리케이션 이벤트로 발행하지 않는다" {
            val applicationEventPublisher = mockk<ApplicationEventPublisher>(relaxed = true)
            val outbox = mockk<Outbox>(relaxed = true)

            DefaultDomainEventPublisher(applicationEventPublisher, outbox).publishExternalEvent(event)

            verify { outbox.schedule(event, "1") }
            verify(exactly = 0) { applicationEventPublisher.publishEvent(any<Any>()) }
        }
    })

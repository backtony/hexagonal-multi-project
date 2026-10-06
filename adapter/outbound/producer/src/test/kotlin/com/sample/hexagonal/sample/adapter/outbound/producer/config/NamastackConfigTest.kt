package com.sample.hexagonal.sample.adapter.outbound.producer.config

import com.sample.hexagonal.sample.domain.event.DomainEvent
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.namastack.outbox.handler.OutboxRecordMetadata
import java.time.Instant

class NamastackConfigTest :
    StringSpec({
        val routing = NamastackConfig().kafkaOutboxRouting()
        val metadata = OutboxRecordMetadata(key = "1", handlerId = "handler", createdAt = Instant.now(), context = emptyMap())

        "도메인 이벤트는 outbox key 로 보낸다" {
            val event = mockk<DomainEvent>()

            routing.shouldExternalize(event, metadata) shouldBe true
            routing.extractKey(event, metadata) shouldBe "1"
        }

        "도메인 이벤트가 아니면 보내지 않는다" {
            routing.shouldExternalize("not an event", metadata) shouldBe false
        }
    })

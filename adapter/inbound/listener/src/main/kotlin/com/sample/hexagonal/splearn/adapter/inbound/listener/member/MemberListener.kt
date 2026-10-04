package com.sample.hexagonal.splearn.adapter.inbound.listener.member

import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberNotifier
import com.sample.hexagonal.splearn.application.support.message.InternalTopic
import com.sample.hexagonal.splearn.domain.member.event.MemberRegisteredEvent
import com.sample.hexagonal.splearn.infrastructure.kafka.consumer.KafkaListenerContainFactoryNames
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@KafkaListener(
    groupId = InternalTopic.MEMBER,
    topics = [InternalTopic.MEMBER],
    containerFactory = KafkaListenerContainFactoryNames.COMMON,
)
class MemberListener(private val memberNotifier: MemberNotifier) {
    private val log = KotlinLogging.logger { }

    @KafkaHandler
    fun handleEvent(event: MemberRegisteredEvent) {
        log.info { "Event received : $event" }
        memberNotifier.sendWelcomeEmail(event.memberId)
    }
}

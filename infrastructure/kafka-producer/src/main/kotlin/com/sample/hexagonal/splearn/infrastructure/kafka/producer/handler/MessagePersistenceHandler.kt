package com.sample.hexagonal.splearn.infrastructure.kafka.producer.handler

import com.sample.hexagonal.splearn.infrastructure.kafka.producer.KafkaMessage

interface MessagePersistenceHandler {
    fun handlePersistence(message: KafkaMessage)
}

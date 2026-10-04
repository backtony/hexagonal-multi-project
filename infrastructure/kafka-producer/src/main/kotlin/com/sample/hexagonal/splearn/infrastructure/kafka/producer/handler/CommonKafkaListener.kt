package com.sample.hexagonal.splearn.infrastructure.kafka.producer.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.kafka.support.ProducerListener

class CommonKafkaListener : ProducerListener<String, Any> {

    private val log = KotlinLogging.logger { }

    override fun onError(producerRecord: ProducerRecord<String, Any>, recordMetadata: RecordMetadata?, exception: Exception) {
        log.error(exception) {
            "Fail to send kafka Message. Topic: ${producerRecord.topic()}, Partition: ${producerRecord.partition()}," +
                " Key: ${producerRecord.key()},  Value: ${producerRecord.value()}"
        }
    }
}

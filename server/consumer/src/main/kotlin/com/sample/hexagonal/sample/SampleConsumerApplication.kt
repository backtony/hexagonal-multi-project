package com.sample.hexagonal.sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SampleConsumerApplication

fun main(args: Array<String>) {
    runApplication<SampleConsumerApplication>(*args)
}

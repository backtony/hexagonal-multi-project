package com.sample.hexagonal.splearn

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SplearnConsumerApplication

fun main(args: Array<String>) {
    runApplication<SplearnConsumerApplication>(*args)
}

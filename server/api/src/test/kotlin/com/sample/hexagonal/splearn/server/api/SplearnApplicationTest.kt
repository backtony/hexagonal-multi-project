package com.sample.hexagonal.splearn.server.api

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

@SpringBootTest
class SplearnApplicationTest(private val environment: Environment) :
    StringSpec({
        "contextLoads" { }

        "infrastructure 모듈 설정을 불러온다" {
            environment.getProperty("spring.datasource.url") shouldBe "jdbc:h2:mem:test;MODE=MySQL"
        }
    })

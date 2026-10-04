package com.sample.hexagonal.splearn.adapter.outbound.security

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class SecurePasswordEncoderTest :
    StringSpec({
        "securePasswordEncoder" {
            val securePasswordEncoder = SecurePasswordEncoder()

            val passwordHash = securePasswordEncoder.encode("secret")

            securePasswordEncoder.matches("secret", passwordHash) shouldBe true
            securePasswordEncoder.matches("wrong", passwordHash) shouldBe false
        }
    })

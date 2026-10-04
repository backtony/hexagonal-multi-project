package com.sample.hexagonal.splearn.domain.member

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EmailTest :
    StringSpec({
        "equality" {
            Email("toby@splearn.app") shouldBe Email("toby@splearn.app")
        }
    })

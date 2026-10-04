package com.sample.hexagonal.splearn.domain.member

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProfileTest :
    StringSpec({
        "profile" {
            Profile("tobyilee")
            Profile("toby100")
            Profile("12345")
            Profile("")
        }

        "profileFail" {
            shouldThrow<IllegalArgumentException> { Profile("toolongtoolongtoolong") }
            shouldThrow<IllegalArgumentException> { Profile("A") }
            shouldThrow<IllegalArgumentException> { Profile("프로필") }
        }

        "url" {
            Profile("tobyilee").url() shouldBe "@tobyilee"
        }
    })

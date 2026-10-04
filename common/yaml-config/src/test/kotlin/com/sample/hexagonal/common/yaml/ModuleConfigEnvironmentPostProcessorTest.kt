package com.sample.hexagonal.common.yaml

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.StandardEnvironment

class ModuleConfigEnvironmentPostProcessorTest :
    StringSpec({
        fun environment(vararg profiles: String) = StandardEnvironment().apply {
            setActiveProfiles(*profiles)
            ModuleConfigEnvironmentPostProcessor().postProcessEnvironment(this, null)
        }

        "profile 이 없는 문서는 항상 적용한다" {
            environment().getProperty("common") shouldBe "always"
        }

        "활성 profile 과 맞는 문서만 적용한다" {
            environment("local").getProperty("profiled") shouldBe "local"
            environment("prod").getProperty("profiled") shouldBe "remote"
            environment("other").getProperty("profiled") shouldBe null
        }

        "기존 설정보다 우선하지 않는다" {
            val environment = StandardEnvironment().apply {
                propertySources.addFirst(MapPropertySource("app", mapOf("common" to "app")))
                ModuleConfigEnvironmentPostProcessor().postProcessEnvironment(this, null)
            }

            environment.getProperty("common") shouldBe "app"
        }
    })

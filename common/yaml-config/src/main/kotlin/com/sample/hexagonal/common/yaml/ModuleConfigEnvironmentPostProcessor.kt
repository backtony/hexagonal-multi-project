package com.sample.hexagonal.common.yaml

import org.springframework.boot.SpringApplication
import org.springframework.boot.context.properties.bind.Binder
import org.springframework.boot.context.properties.source.ConfigurationPropertySource
import org.springframework.boot.EnvironmentPostProcessor
import org.springframework.boot.env.YamlPropertySourceLoader
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.Profiles
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

/**
 * classpath 의 config 디렉터리에 있는 yml 을 모두 읽어 우선순위가 가장 낮은 기본값으로 등록한다.
 * 문서별 `spring.config.activate.on-profile` 이 활성 profile 과 맞을 때만 적용하고, 없으면 항상 적용한다.
 */
class ModuleConfigEnvironmentPostProcessor : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
        val loader = YamlPropertySourceLoader()
        PathMatchingResourcePatternResolver().getResources("classpath*:config/*.yml")
            .flatMap { loader.load("config: ${it.filename}", it) }
            .filter { it.isActiveFor(environment) }
            .forEach(environment.propertySources::addLast)
    }

    private fun PropertySource<*>.isActiveFor(environment: ConfigurableEnvironment): Boolean {
        val source = ConfigurationPropertySource.from(this) ?: return true
        val onProfile = Binder(source)
            .bind("spring.config.activate.on-profile", Array<String>::class.java)
            .orElse(null) ?: return true
        return environment.acceptsProfiles(Profiles.of(*onProfile))
    }
}

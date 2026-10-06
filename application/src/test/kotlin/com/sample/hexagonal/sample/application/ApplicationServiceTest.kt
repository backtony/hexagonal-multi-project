package com.sample.hexagonal.sample.application

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest
@Transactional
@Import(RecordingDomainEventPublisher::class)
annotation class ApplicationServiceTest

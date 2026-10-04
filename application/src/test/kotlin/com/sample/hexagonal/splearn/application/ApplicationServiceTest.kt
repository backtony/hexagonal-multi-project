package com.sample.hexagonal.splearn.application

import com.sample.hexagonal.splearn.application.SplearnTestConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@SpringBootTest
@Transactional
@Import(
    SplearnTestConfiguration::class,
    ApplicationTestDataPreparer::class,
    RecordingDomainEventPublisher::class,
)
annotation class ApplicationServiceTest

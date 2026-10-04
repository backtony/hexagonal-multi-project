package com.sample.hexagonal.splearn.adapter.outbound.repository

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@DataJpaTest
@Import(RepositoryTestDataPreparer::class)
annotation class RepositoryTest

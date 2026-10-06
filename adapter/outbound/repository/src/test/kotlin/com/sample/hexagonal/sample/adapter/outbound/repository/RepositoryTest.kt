package com.sample.hexagonal.sample.adapter.outbound.repository

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@DataJpaTest
annotation class RepositoryTest

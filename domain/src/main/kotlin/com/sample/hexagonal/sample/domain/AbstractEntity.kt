package com.sample.hexagonal.sample.domain

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        protected set

    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    var updatedAt: LocalDateTime = createdAt
        protected set

    var deleted: Boolean = false
        protected set

    protected fun markUpdated() {
        updatedAt = LocalDateTime.now()
    }

    internal fun markDeleted() {
        deleted = true
        markUpdated()
    }
}

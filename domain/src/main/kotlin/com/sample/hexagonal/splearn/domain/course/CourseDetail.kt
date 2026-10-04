package com.sample.hexagonal.splearn.domain.course

import com.sample.hexagonal.splearn.domain.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.LocalDateTime

@Entity
class CourseDetail internal constructor(description: String?) : AbstractEntity() {
    @Column(length = 500)
    var description: String? = description
        protected set

    var publishedAt: LocalDateTime? = null
        protected set

    var archivedAt: LocalDateTime? = null
        protected set

    internal fun publish() {
        publishedAt = LocalDateTime.now()
        markUpdated()
    }

    internal fun archive() {
        archivedAt = LocalDateTime.now()
        markUpdated()
    }

    internal fun updateInfo(updateInfo: CourseUpdateInfo) {
        description = updateInfo.description
        markUpdated()
    }
}

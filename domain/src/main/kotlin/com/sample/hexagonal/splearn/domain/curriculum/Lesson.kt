package com.sample.hexagonal.splearn.domain.curriculum

import com.sample.hexagonal.splearn.domain.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne

@Entity
class Lesson internal constructor(
    section: Section,
    title: String,
) : AbstractEntity() {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    var section: Section = section
        protected set

    @Column(length = 200)
    var title: String = title
        protected set

    internal fun updateTitle(title: String) {
        this.title = title
        markUpdated()
    }

    internal fun moveTo(section: Section) {
        this.section = section
        markUpdated()
    }
}

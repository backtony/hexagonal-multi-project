package com.sample.hexagonal.splearn.domain.course

import com.sample.hexagonal.splearn.domain.AggregateRoot
import com.sample.hexagonal.splearn.domain.course.event.CourseCreatedEvent
import com.sample.hexagonal.splearn.domain.event.AbstractAggregateRoot
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.PostPersist
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@AggregateRoot
@Entity
@Table(
    name = "course",
    uniqueConstraints = [UniqueConstraint(name = "UK_INSTRUCTOR_TITLE", columnNames = ["instructor_id", "title"])],
)
class Course(
    instructor: Instructor,
    title: String,
    description: String?,
) : AbstractAggregateRoot() {
    init {
        instructor.ensureActive()
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val instructor: Instructor = instructor

    @Column(nullable = false, length = 100)
    var title: String = title
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: CourseStatus = CourseStatus.DRAFT
        protected set

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val detail: CourseDetail = CourseDetail(description)

    @PostPersist
    protected fun onCreated() {
        registerEvent(CourseCreatedEvent(id!!))
    }

    fun submitForReview() {
        check(status == CourseStatus.DRAFT) { "DRAFT 상태가 아닙니다" }
        check(!detail.description.isNullOrBlank()) { "강의 소개가 등록되지 않았습니다" }

        status = CourseStatus.IN_REVIEW
        markUpdated()
    }

    fun publish() {
        check(status == CourseStatus.IN_REVIEW) { "IN_REVIEW 상태가 아닙니다" }

        status = CourseStatus.PUBLISHED
        detail.publish()
        markUpdated()
    }

    fun archive() {
        check(status == CourseStatus.PUBLISHED) { "PUBLISHED 상태가 아닙니다" }

        status = CourseStatus.ARCHIVED
        detail.archive()
        markUpdated()
    }

    fun isPublished(): Boolean = status == CourseStatus.PUBLISHED

    fun ensurePublished() {
        check(status == CourseStatus.PUBLISHED) { "PUBLISHED 상태가 아닙니다" }
    }

    fun updateInfo(updateInfo: CourseUpdateInfo) {
        title = updateInfo.title
        detail.updateInfo(updateInfo)
        markUpdated()
    }
}

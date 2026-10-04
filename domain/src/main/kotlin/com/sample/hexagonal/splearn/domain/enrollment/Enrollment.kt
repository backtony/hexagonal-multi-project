package com.sample.hexagonal.splearn.domain.enrollment

import com.sample.hexagonal.splearn.domain.AbstractEntity
import com.sample.hexagonal.splearn.domain.AggregateRoot
import com.sample.hexagonal.splearn.domain.course.Course
import com.sample.hexagonal.splearn.domain.member.Member
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@AggregateRoot
@Entity
@Table(uniqueConstraints = [UniqueConstraint(name = "UK_ENROLLMENT_MEMBER_COURSE", columnNames = ["member_id", "course_id"])])
class Enrollment protected constructor(
    member: Member,
    course: Course,
) : AbstractEntity() {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val member: Member = member

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val course: Course = course

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: EnrollmentStatus = EnrollmentStatus.ENROLLED
        protected set

    var completedAt: LocalDateTime? = null
        protected set

    fun startStudying() {
        check(status == EnrollmentStatus.ENROLLED) { "수강 상태가 ENROLLED가 아닙니다" }

        status = EnrollmentStatus.STUDYING
        markUpdated()
    }

    fun complete() {
        check(status == EnrollmentStatus.STUDYING) { "수강 상태가 STUDYING이 아닙니다" }

        status = EnrollmentStatus.COMPLETED
        completedAt = LocalDateTime.now()
        markUpdated()
    }

    companion object {
        fun enroll(member: Member, course: Course): Enrollment {
            member.ensureActive()
            course.ensurePublished()

            return Enrollment(member, course)
        }
    }
}

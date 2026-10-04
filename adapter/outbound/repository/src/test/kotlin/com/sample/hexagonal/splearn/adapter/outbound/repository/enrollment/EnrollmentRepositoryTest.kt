package com.sample.hexagonal.splearn.adapter.outbound.repository.enrollment

import com.sample.hexagonal.splearn.application.service.enrollment.port.outbound.EnrollmentRepository
import com.sample.hexagonal.splearn.domain.enrollment.Enrollment
import com.sample.hexagonal.splearn.adapter.outbound.repository.RepositoryTest
import com.sample.hexagonal.splearn.adapter.outbound.repository.RepositoryTestDataPreparer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager

@RepositoryTest
class EnrollmentRepositoryTest(
    enrollmentRepository: EnrollmentRepository,
    entityManager: EntityManager,
    preparer: RepositoryTestDataPreparer,
) : StringSpec({
    "saveAndFindId" {
        val member = preparer.prepareActiveMember()
        val course = preparer.preparePublishedCourse()

        val enrollment = enrollmentRepository.save(Enrollment.enroll(member, course))
        enrollment.id.shouldNotBeNull()

        entityManager.flush()
        entityManager.clear()

        enrollmentRepository.findById(enrollment.id!!)?.id shouldBe enrollment.id
    }

    "findByMemberId" {
        val member1 = preparer.prepareActiveMember()
        val member2 = preparer.prepareActiveMember()
        val enrollment11 = preparer.prepareEnrollment(member1, preparer.preparePublishedCourse())
        val enrollment12 = preparer.prepareEnrollment(member1, preparer.preparePublishedCourse())
        val enrollment2 = preparer.prepareEnrollment(member2, preparer.preparePublishedCourse())

        enrollmentRepository.findByMemberId(member1.id!!) shouldContainExactly listOf(enrollment11, enrollment12)
        enrollmentRepository.findByMemberId(member2.id!!) shouldContainExactly listOf(enrollment2)
    }

    "findByMemberIdAndCourseId" {
        val member1 = preparer.prepareActiveMember()
        val member2 = preparer.prepareActiveMember()
        val course1 = preparer.preparePublishedCourse()
        val course2 = preparer.preparePublishedCourse()
        val enrollment1 = preparer.prepareEnrollment(member1, course1)
        val enrollment2 = preparer.prepareEnrollment(member2, course2)

        enrollmentRepository.findByMemberIdAndCourseId(member1.id!!, course1.id!!) shouldBe enrollment1
        enrollmentRepository.findByMemberIdAndCourseId(member2.id!!, course2.id!!) shouldBe enrollment2
        enrollmentRepository.findByMemberIdAndCourseId(member1.id!!, course2.id!!).shouldBeNull()
    }
})

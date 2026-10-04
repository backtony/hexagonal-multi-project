package com.sample.hexagonal.splearn.application.service.course

import com.sample.hexagonal.splearn.application.service.course.port.inbound.CoursePublisher
import com.sample.hexagonal.splearn.domain.course.CourseStatus
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.ApplicationTestDataPreparer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

@ApplicationServiceTest
class CoursePublisherTest(
    coursePublisher: CoursePublisher,
    preparer: ApplicationTestDataPreparer,
) : StringSpec({
    "submitForReview" {
        val course = preparer.prepareCourseWithCurriculum()

        coursePublisher.submitForReview(course.id!!).status shouldBe CourseStatus.IN_REVIEW
    }

    "publish" {
        val course = preparer.prepareCourseWithCurriculum()
        coursePublisher.submitForReview(course.id!!)

        coursePublisher.publish(course.id!!).status shouldBe CourseStatus.PUBLISHED
    }

    "archive" {
        val course = preparer.prepareCourseWithCurriculum()
        coursePublisher.submitForReview(course.id!!)
        coursePublisher.publish(course.id!!)

        coursePublisher.archive(course.id!!).status shouldBe CourseStatus.ARCHIVED
    }
})

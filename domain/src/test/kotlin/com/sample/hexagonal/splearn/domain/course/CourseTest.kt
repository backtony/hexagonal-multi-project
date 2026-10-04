package com.sample.hexagonal.splearn.domain.course

import com.sample.hexagonal.splearn.domain.instructor.InstructorFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class CourseTest :
    StringSpec({
        lateinit var course: Course

        beforeTest {
            course = CourseFixture.createCourse()
            course.updateInfo(CourseUpdateInfo(course.title, "Description"))
        }

        "create" {
            val instructor = InstructorFixture.createActiveInstructor()

            val course = Course(instructor, "Clean Spring 2", "Description")

            course.instructor shouldBe instructor
            course.title shouldBe "Clean Spring 2"
            course.status shouldBe CourseStatus.DRAFT
            course.detail.description shouldBe "Description"
            course.detail.createdAt.shouldNotBeNull()
        }

        "createFailNotActiveInstructor" {
            val instructor = InstructorFixture.createInstructor()

            shouldThrow<IllegalStateException> { Course(instructor, "title", null) }
        }

        "submitForReview" {
            course.submitForReview()

            course.status shouldBe CourseStatus.IN_REVIEW
            shouldThrow<IllegalStateException> { course.submitForReview() }
        }

        "submitForReviewFail" {
            val course = Course(InstructorFixture.createActiveInstructor(), "Clean Spring 2", null)

            shouldThrow<IllegalStateException> { course.submitForReview() }
        }

        "publish" {
            course.submitForReview()
            course.publish()

            course.status shouldBe CourseStatus.PUBLISHED
            course.detail.publishedAt.shouldNotBeNull()
            shouldThrow<IllegalStateException> { course.publish() }
        }

        "archive" {
            course.submitForReview()
            course.publish()
            course.archive()

            course.status shouldBe CourseStatus.ARCHIVED
            course.detail.archivedAt.shouldNotBeNull()
            shouldThrow<IllegalStateException> { course.archive() }
        }

        "updateInfo" {
            course.updateInfo(CourseUpdateInfo("Clean Spring 3", "Updated Description"))

            course.title shouldBe "Clean Spring 3"
            course.detail.description shouldBe "Updated Description"
        }
    })

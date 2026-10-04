package com.sample.hexagonal.splearn.application.service.course

import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreateRequest
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CoursePublisher
import com.sample.hexagonal.splearn.application.service.course.port.outbound.CourseRepository
import com.sample.hexagonal.splearn.application.support.exception.ValidationException
import com.sample.hexagonal.splearn.domain.course.CourseFixture
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.ApplicationTestDataPreparer
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseRequestFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize

@ApplicationServiceTest
class CourseValidatorTest(
    courseValidator: CourseValidator,
    courseRepository: CourseRepository,
    coursePublisher: CoursePublisher,
    preparer: ApplicationTestDataPreparer,
) : StringSpec({
    "titleDuplicationForCreate" {
        val instructor1 = preparer.prepareInstructor()
        val instructor2 = preparer.prepareInstructor()
        courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Spring"))
        courseRepository.save(CourseFixture.createCourse(instructor2, "Clean Code"))

        // instructor1, 중복되지 않는 제목 - OK
        courseValidator.validateForCreate(instructor1, CourseCreateRequest(instructor1.id!!, "Spring 7", null))

        // instructor1, 중복 제목 - FAIL
        shouldThrow<ValidationException> {
            courseValidator.validateForCreate(instructor1, CourseCreateRequest(instructor1.id!!, "Clean Spring", null))
        }.errors shouldHaveSize 1

        // instructor2, 1과 중복되는 제목 - OK
        courseValidator.validateForCreate(instructor2, CourseCreateRequest(instructor2.id!!, "Clean Spring", null))
    }

    "titleDuplicationForUpdate" {
        val instructor1 = preparer.prepareInstructor()
        val instructor2 = preparer.prepareInstructor()
        val course11 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Spring"))
        val course12 = courseRepository.save(CourseFixture.createCourse(instructor1, "Clean Code"))
        courseRepository.save(CourseFixture.createCourse(instructor2, "Clean Spring"))

        // title 변경 없이 update - OK
        courseValidator.validateForUpdate(course11, CourseRequestFixture.createCourseInfoUpdateRequest(course11.title))

        // title 변경하는데 중복 발생 - FAIL
        shouldThrow<ValidationException> {
            courseValidator.validateForUpdate(course11, CourseRequestFixture.createCourseInfoUpdateRequest(course12.title))
        }.errors shouldHaveSize 1
    }

    "submitForReviewFailNoCurriculum" {
        val course = preparer.prepareCourse()

        shouldThrow<ValidationException> { coursePublisher.submitForReview(course.id!!) }.errors shouldHaveSize 1
    }

    "submitForReviewFailInvalidCurriculum" {
        val course = preparer.prepareCourse()
        val curriculum = preparer.prepareCurriculumSectionsAndLessons(course)
        curriculum.removeLesson(2, 0)

        shouldThrow<ValidationException> { coursePublisher.submitForReview(course.id!!) }
    }

    "publishFailInvalidCurriculum" {
        val course = preparer.prepareCourse()
        val curriculum = preparer.prepareCurriculumSectionsAndLessons(course)
        coursePublisher.submitForReview(course.id!!)
        curriculum.removeLesson(2, 0)

        shouldThrow<ValidationException> { coursePublisher.publish(course.id!!) }
    }
})

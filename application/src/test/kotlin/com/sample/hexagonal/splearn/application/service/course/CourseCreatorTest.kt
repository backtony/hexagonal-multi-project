package com.sample.hexagonal.splearn.application.service.course

import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreator
import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.ApplicationTestDataPreparer
import com.sample.hexagonal.splearn.application.RecordingDomainEventPublisher
import com.sample.hexagonal.splearn.domain.course.event.CourseCreatedEvent
import io.kotest.matchers.collections.shouldContainExactly
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseRequestFixture
import io.kotest.core.spec.style.StringSpec
import com.sample.hexagonal.splearn.application.service.curriculum.port.outbound.CurriculumRepository
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@ApplicationServiceTest
class CourseCreatorTest(
    courseCreator: CourseCreator,
    curriculumRepository: CurriculumRepository,
    domainEventPublisher: RecordingDomainEventPublisher,
    preparer: ApplicationTestDataPreparer,
) : StringSpec({
    "create" {
        val instructor = preparer.prepareInstructor()

        val course = courseCreator.create(CourseRequestFixture.createCourseCreateRequest(instructor.id!!))

        course.id.shouldNotBeNull()
        curriculumRepository.findByCourseId(course.id!!).shouldBeNull()
    }

    "createPublishesCourseCreatedEvent" {
        val instructor = preparer.prepareInstructor()
        domainEventPublisher.events.clear()

        val course = courseCreator.create(CourseRequestFixture.createCourseCreateRequest(instructor.id!!))

        domainEventPublisher.events.filterIsInstance<CourseCreatedEvent>().map { it.courseId } shouldContainExactly listOf(course.id)
    }

    "updateInfo" {
        val instructor = preparer.prepareInstructor()
        val course = courseCreator.create(CourseRequestFixture.createCourseCreateRequest(instructor.id!!))

        val updated = courseCreator.updateInfo(course.id!!, CourseRequestFixture.createCourseInfoUpdateRequest("Updated"))

        updated.title shouldBe "Updated"
    }
})

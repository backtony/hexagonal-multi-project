package com.sample.hexagonal.splearn.adapter.outbound.repository.course

import com.sample.hexagonal.splearn.application.service.course.port.outbound.CourseRepository
import com.sample.hexagonal.splearn.domain.course.CourseFixture
import com.sample.hexagonal.splearn.domain.course.event.CourseCreatedEvent
import com.sample.hexagonal.splearn.adapter.outbound.repository.RepositoryTest
import com.sample.hexagonal.splearn.adapter.outbound.repository.RepositoryTestDataPreparer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager
import org.springframework.dao.DataIntegrityViolationException

@RepositoryTest
class CourseRepositoryTest(
    courseRepository: CourseRepository,
    entityManager: EntityManager,
    preparer: RepositoryTestDataPreparer,
) : StringSpec({
    "saveRegistersCourseCreatedEvent" {
        val course = courseRepository.save(CourseFixture.createCourse(preparer.prepareActiveInstructor()))

        course.pullDomainEvents().map { (it as CourseCreatedEvent).courseId } shouldBe listOf(course.id)
        course.pullDomainEvents() shouldBe emptyList()
    }

    "saveAndFindId" {
        val instructor = preparer.prepareActiveInstructor()

        val course = courseRepository.save(CourseFixture.createCourse(instructor))
        course.id.shouldNotBeNull()

        entityManager.flush()
        entityManager.clear()

        courseRepository.findById(course.id!!)?.id shouldBe course.id
    }

    "findByTitleContaining" {
        val instructor = preparer.prepareActiveInstructor()
        val ids = listOf(
            preparer.prepareCourse(instructor, "Hello Spring"),
            preparer.prepareCourse(instructor, "Clean Spring 2"),
            preparer.prepareCourse(instructor, "Clean Code"),
        ).map { it.id }

        courseRepository.findByTitleContaining("Spring").map { it.id } shouldContainExactly listOf(ids[0], ids[1])
        courseRepository.findByTitleContaining("Clean").map { it.id } shouldContainExactly listOf(ids[1], ids[2])
        courseRepository.findByTitleContaining("Code").map { it.id } shouldContainExactly listOf(ids[2])
        courseRepository.findByTitleContaining("JPA").shouldBeEmpty()
    }

    "findByInstructor" {
        val instructor1 = preparer.prepareActiveInstructor()
        val instructor2 = preparer.prepareActiveInstructor()
        val course = preparer.prepareCourse(instructor1)
        val course2 = preparer.prepareCourse(instructor2)

        courseRepository.findByInstructorId(instructor1.id!!) shouldContainExactly listOf(course)
        courseRepository.findByInstructorId(instructor2.id!!) shouldContainExactly listOf(course2)
    }

    "uniqueTitleAndInstructor" {
        val instructor = preparer.prepareActiveInstructor()
        preparer.prepareCourse(instructor, "Title")

        shouldThrow<DataIntegrityViolationException> {
            courseRepository.save(CourseFixture.createCourse(instructor, "Title"))
        }
    }
})

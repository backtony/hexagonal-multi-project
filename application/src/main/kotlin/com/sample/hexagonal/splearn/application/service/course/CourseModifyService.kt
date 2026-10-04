package com.sample.hexagonal.splearn.application.service.course

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreator
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreateRequest
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseInfoUpdateRequest
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CoursePublisher
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorFinder
import com.sample.hexagonal.splearn.application.support.event.DomainEventPublisher
import com.sample.hexagonal.splearn.application.service.course.port.outbound.CourseRepository
import com.sample.hexagonal.splearn.domain.course.Course
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Transactional
@Validated
class CourseModifyService(
    private val courseRepository: CourseRepository,
    private val courseValidator: CourseValidator,
    private val instructorFinder: InstructorFinder,
    private val domainEventPublisher: DomainEventPublisher,
) : CourseCreator,
    CoursePublisher {
    override fun create(createRequest: CourseCreateRequest): Course {
        val instructor = instructorFinder.find(createRequest.instructorId)

        courseValidator.validateForCreate(instructor, createRequest)

        val course = courseRepository.save(Course(instructor, createRequest.title, createRequest.description))
        course.pullDomainEvents().forEach(domainEventPublisher::publishEvent)

        return course
    }

    override fun updateInfo(courseId: Long, infoUpdateRequest: CourseInfoUpdateRequest): Course {
        val course = courseRepository.findById(courseId).getOrThrowNotFound(courseId)

        courseValidator.validateForUpdate(course, infoUpdateRequest)

        course.updateInfo(infoUpdateRequest.toInfo())

        return courseRepository.save(course)
    }

    override fun submitForReview(courseId: Long): Course {
        val course = courseRepository.findById(courseId).getOrThrowNotFound(courseId)

        courseValidator.validateForReview(course)

        course.submitForReview()

        return courseRepository.save(course)
    }

    override fun publish(courseId: Long): Course {
        val course = courseRepository.findById(courseId).getOrThrowNotFound(courseId)

        courseValidator.validateForPublish(course)

        course.publish()

        return courseRepository.save(course)
    }

    override fun archive(courseId: Long): Course {
        val course = courseRepository.findById(courseId).getOrThrowNotFound(courseId)

        courseValidator.validateForArchive(course)

        course.archive()

        return courseRepository.save(course)
    }
}

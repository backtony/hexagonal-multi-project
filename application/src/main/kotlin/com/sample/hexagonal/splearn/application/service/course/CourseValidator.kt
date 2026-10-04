package com.sample.hexagonal.splearn.application.service.course

import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseCreateRequest
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseInfoUpdateRequest
import com.sample.hexagonal.splearn.application.service.course.port.outbound.CourseRepository
import com.sample.hexagonal.splearn.application.service.course.spi.CurriculumValidator
import com.sample.hexagonal.splearn.application.support.exception.ValidationException
import com.sample.hexagonal.splearn.domain.course.Course
import com.sample.hexagonal.splearn.domain.curriculum.InvalidCurriculumException
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import org.springframework.stereotype.Component

@Component
class CourseValidator(
    private val courseRepository: CourseRepository,
    private val curriculumValidator: CurriculumValidator,
) {
    fun validateForCreate(instructor: Instructor, createRequest: CourseCreateRequest) {
        instructor.ensureActive()

        val errors = mutableListOf<String>()
        checkTitleDuplicationForCreate(instructor, createRequest.title, errors)
        throwIfAny(errors)
    }

    fun validateForUpdate(course: Course, infoUpdateRequest: CourseInfoUpdateRequest) {
        val errors = mutableListOf<String>()
        checkTitleDuplicationForUpdate(course, infoUpdateRequest.title, errors)
        throwIfAny(errors)
    }

    fun validateForReview(course: Course) {
        val errors = mutableListOf<String>()
        checkCurriculum(course, errors)
        throwIfAny(errors)
    }

    fun validateForPublish(course: Course) {
        val errors = mutableListOf<String>()
        checkCurriculum(course, errors)
        throwIfAny(errors)
    }

    fun validateForArchive(course: Course) {
    }

    private fun checkCurriculum(course: Course, errors: MutableList<String>) {
        try {
            curriculumValidator.validate(course.id!!)
        } catch (e: InvalidCurriculumException) {
            errors.add(e.message!!)
        }
    }

    private fun checkTitleDuplicationForCreate(instructor: Instructor, title: String, errors: MutableList<String>) {
        if (courseRepository.findByInstructorAndTitle(instructor, title) != null) {
            errors.add("이미 사용중인 강의 제목입니다. $title")
        }
    }

    private fun checkTitleDuplicationForUpdate(course: Course, title: String, errors: MutableList<String>) {
        val found = courseRepository.findByInstructorAndTitle(course.instructor, title)
        if (found != null && found != course) {
            errors.add("이미 사용중인 강의 제목입니다. $title")
        }
    }

    private fun throwIfAny(errors: List<String>) {
        if (errors.isNotEmpty()) throw ValidationException(errors)
    }
}

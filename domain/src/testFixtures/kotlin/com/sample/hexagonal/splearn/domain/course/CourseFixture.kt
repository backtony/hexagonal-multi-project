package com.sample.hexagonal.splearn.domain.course

import com.sample.hexagonal.splearn.domain.instructor.Instructor
import com.sample.hexagonal.splearn.domain.instructor.InstructorFixture
import org.instancio.Instancio

object CourseFixture {
    fun createCourse(instructor: Instructor? = null, title: String? = null): Course = Course(
        instructor ?: InstructorFixture.createActiveInstructor(),
        title ?: createTitle(),
        Instancio.gen().string().maxLength(500).nullable().get(),
    )

    fun createPublishedCourse(): Course = createCourse().apply {
        updateInfo(CourseUpdateInfo(title, Instancio.gen().string().minLength(1).maxLength(500).get()))
        submitForReview()
        publish()
    }

    fun createTitle(): String = Instancio.gen().string().minLength(2).maxLength(100).get()
}

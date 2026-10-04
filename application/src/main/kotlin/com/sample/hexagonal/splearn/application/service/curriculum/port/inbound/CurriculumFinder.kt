package com.sample.hexagonal.splearn.application.service.curriculum.port.inbound

import com.sample.hexagonal.splearn.domain.curriculum.Curriculum
import com.sample.hexagonal.splearn.domain.curriculum.Lesson

interface CurriculumFinder {
    fun find(curriculumId: Long): Curriculum

    fun findWithSections(curriculumId: Long): Curriculum

    fun findByCourse(courseId: Long): Curriculum

    fun firstLesson(curriculumId: Long): Lesson?

    fun nextLesson(curriculumId: Long, lessonId: Long): Lesson?
}

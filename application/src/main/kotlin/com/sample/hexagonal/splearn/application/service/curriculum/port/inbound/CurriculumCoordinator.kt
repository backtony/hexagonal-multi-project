package com.sample.hexagonal.splearn.application.service.curriculum.port.inbound

import com.sample.hexagonal.splearn.domain.curriculum.Curriculum

interface CurriculumCoordinator {
    fun createCurriculum(courseId: Long): Curriculum

    fun addSection(curriculumId: Long, title: String): Curriculum

    fun addSection(curriculumId: Long, sectionIndex: Int, title: String): Curriculum

    fun addLesson(curriculumId: Long, sectionIndex: Int, title: String): Curriculum

    fun updateSectionTitle(curriculumId: Long, sectionIndex: Int, title: String): Curriculum

    fun updateLessonTitle(curriculumId: Long, sectionIndex: Int, lessonIndex: Int, title: String): Curriculum

    fun removeLesson(curriculumId: Long, sectionIndex: Int, lessonIndex: Int): Curriculum

    fun removeSection(curriculumId: Long, sectionIndex: Int): Curriculum

    fun moveLesson(curriculumId: Long, fromSectionIndex: Int, fromLessonIndex: Int, toSectionIndex: Int, toLessonIndex: Int): Curriculum
}

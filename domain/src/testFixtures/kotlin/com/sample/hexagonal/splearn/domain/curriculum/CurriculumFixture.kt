package com.sample.hexagonal.splearn.domain.curriculum

import com.sample.hexagonal.splearn.domain.course.CourseFixture

object CurriculumFixture {
    fun createCurriculum(): Curriculum = Curriculum(CourseFixture.createCourse())
}

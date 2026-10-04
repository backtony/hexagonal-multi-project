package com.sample.hexagonal.splearn.application.service.curriculum.port.outbound

import com.sample.hexagonal.splearn.domain.curriculum.Curriculum

interface CurriculumRepository {
    fun save(curriculum: Curriculum): Curriculum

    fun findById(curriculumId: Long): Curriculum?

    fun findWithSectionsById(curriculumId: Long): Curriculum?

    fun findByCourseId(courseId: Long): Curriculum?
}

package com.sample.hexagonal.splearn.adapter.outbound.repository.curriculum

import com.sample.hexagonal.splearn.application.service.curriculum.port.outbound.CurriculumRepository
import com.sample.hexagonal.splearn.domain.curriculum.Curriculum
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.Repository

interface CurriculumJpaRepository :
    Repository<Curriculum, Long>,
    CurriculumRepository {
    @EntityGraph(attributePaths = ["mutableSections", "mutableSections.mutableLessons"])
    override fun findWithSectionsById(curriculumId: Long): Curriculum?
}

package com.sample.hexagonal.splearn.application.service.curriculum

import com.sample.hexagonal.splearn.application.service.course.spi.CurriculumValidator
import com.sample.hexagonal.splearn.application.service.curriculum.port.outbound.CurriculumRepository
import com.sample.hexagonal.splearn.domain.curriculum.InvalidCurriculumException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CurriculumValidationService(private val curriculumRepository: CurriculumRepository) : CurriculumValidator {
    override fun validate(courseId: Long) {
        val curriculum = curriculumRepository.findByCourseId(courseId) ?: throw InvalidCurriculumException("커리큘럼이 등록되지 않았습니다")

        curriculum.validate()
    }
}

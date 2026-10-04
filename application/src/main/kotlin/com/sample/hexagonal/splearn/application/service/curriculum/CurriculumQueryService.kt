package com.sample.hexagonal.splearn.application.service.curriculum

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.curriculum.port.inbound.CurriculumFinder
import com.sample.hexagonal.splearn.application.service.curriculum.port.outbound.CurriculumRepository
import com.sample.hexagonal.splearn.domain.curriculum.Curriculum
import com.sample.hexagonal.splearn.domain.curriculum.Lesson
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CurriculumQueryService(private val curriculumRepository: CurriculumRepository) : CurriculumFinder {
    override fun find(curriculumId: Long): Curriculum = curriculumRepository.findById(curriculumId).getOrThrowNotFound(curriculumId)

    override fun findWithSections(curriculumId: Long): Curriculum =
        curriculumRepository.findWithSectionsById(curriculumId).getOrThrowNotFound(curriculumId)

    override fun findByCourse(courseId: Long): Curriculum =
        curriculumRepository.findByCourseId(courseId).getOrThrowNotFound("courseId=$courseId")

    override fun firstLesson(curriculumId: Long): Lesson? = findWithSections(curriculumId).firstLesson()

    override fun nextLesson(curriculumId: Long, lessonId: Long): Lesson? = findWithSections(curriculumId).nextLesson(lessonId)
}

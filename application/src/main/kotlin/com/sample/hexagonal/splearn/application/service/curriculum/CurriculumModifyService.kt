package com.sample.hexagonal.splearn.application.service.curriculum

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.course.port.inbound.CourseFinder
import com.sample.hexagonal.splearn.application.service.curriculum.port.inbound.CurriculumCoordinator
import com.sample.hexagonal.splearn.application.service.curriculum.port.outbound.CurriculumRepository
import com.sample.hexagonal.splearn.domain.curriculum.Curriculum
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CurriculumModifyService(
    private val curriculumRepository: CurriculumRepository,
    private val courseFinder: CourseFinder,
) : CurriculumCoordinator {
    override fun createCurriculum(courseId: Long): Curriculum =
        curriculumRepository.findByCourseId(courseId) ?: curriculumRepository.save(Curriculum(courseFinder.find(courseId)))

    override fun addSection(curriculumId: Long, title: String): Curriculum = modify(curriculumId) { it.addSection(title) }

    override fun addSection(curriculumId: Long, sectionIndex: Int, title: String): Curriculum =
        modify(curriculumId) { it.addSection(sectionIndex, title) }

    override fun addLesson(curriculumId: Long, sectionIndex: Int, title: String): Curriculum =
        modify(curriculumId) { it.addLesson(sectionIndex, title) }

    override fun updateSectionTitle(curriculumId: Long, sectionIndex: Int, title: String): Curriculum =
        modify(curriculumId) { it.updateSectionTitle(sectionIndex, title) }

    override fun updateLessonTitle(curriculumId: Long, sectionIndex: Int, lessonIndex: Int, title: String): Curriculum =
        modify(curriculumId) { it.updateLessonTitle(sectionIndex, lessonIndex, title) }

    override fun removeLesson(curriculumId: Long, sectionIndex: Int, lessonIndex: Int): Curriculum =
        modify(curriculumId) { it.removeLesson(sectionIndex, lessonIndex) }

    override fun removeSection(curriculumId: Long, sectionIndex: Int): Curriculum = modify(curriculumId) { it.removeSection(sectionIndex) }

    override fun moveLesson(
        curriculumId: Long,
        fromSectionIndex: Int,
        fromLessonIndex: Int,
        toSectionIndex: Int,
        toLessonIndex: Int,
    ): Curriculum = modify(curriculumId) { it.moveLesson(fromSectionIndex, fromLessonIndex, toSectionIndex, toLessonIndex) }

    private fun modify(curriculumId: Long, action: (Curriculum) -> Unit): Curriculum {
        val curriculum = curriculumRepository.findById(curriculumId).getOrThrowNotFound(curriculumId)

        action(curriculum)

        return curriculumRepository.save(curriculum)
    }
}

package com.sample.hexagonal.splearn.domain.curriculum

import com.sample.hexagonal.splearn.domain.AggregateRoot
import com.sample.hexagonal.splearn.domain.event.AbstractAggregateRoot
import com.sample.hexagonal.splearn.domain.course.Course
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.OrderColumn
import java.util.Collections

@AggregateRoot
@Entity
class Curriculum(course: Course) : AbstractAggregateRoot() {
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    val course: Course = course

    @OneToMany(mappedBy = "curriculum", cascade = [CascadeType.ALL])
    @OrderColumn(name = "section_order")
    private val mutableSections: MutableList<Section> = mutableListOf()

    val sections: List<Section>
        get() = Collections.unmodifiableList(mutableSections)

    fun addSection(title: String): Section = Section(this, title).also {
        mutableSections.add(it)
        markUpdated()
    }

    fun addSection(sectionIndex: Int, title: String): Section = Section(this, title).also {
        mutableSections.add(sectionIndex, it)
        markUpdated()
    }

    fun addLesson(sectionIndex: Int, title: String): Lesson = mutableSections[sectionIndex].addLesson(title).also { markUpdated() }

    fun updateSectionTitle(sectionIndex: Int, title: String): Section = mutableSections[sectionIndex].also {
        it.updateTitle(title)
        markUpdated()
    }

    fun updateLessonTitle(sectionIndex: Int, lessonIndex: Int, title: String) {
        mutableSections[sectionIndex].updateLessonTitle(lessonIndex, title)
        markUpdated()
    }

    fun removeLesson(sectionIndex: Int, lessonIndex: Int): Lesson = mutableSections[sectionIndex].removeLesson(lessonIndex).also {
        it.markDeleted()
        markUpdated()
    }

    fun allLessons(): List<Lesson> = mutableSections.flatMap { it.lessons }

    fun removeSection(sectionIndex: Int): Section {
        check(mutableSections.size > 1) { "마지막 남은 섹션은 삭제할 수 없습니다" }

        val removed = mutableSections.removeAt(sectionIndex)
        removed.markDeleted()

        if (sectionIndex == 0) {
            removed.moveAllLessonsTo(mutableSections[0], 0)
        } else {
            val previous = mutableSections[sectionIndex - 1]
            removed.moveAllLessonsTo(previous, previous.lessons.size)
        }

        markUpdated()

        return removed
    }

    fun moveLesson(fromSectionIndex: Int, fromLessonIndex: Int, toSectionIndex: Int, toLessonIndex: Int) {
        val from = mutableSections[fromSectionIndex]
        val to = mutableSections[toSectionIndex]

        to.addLesson(toLessonIndex, from.removeLesson(fromLessonIndex))
        markUpdated()
    }

    fun validate() {
        if (mutableSections.isEmpty()) throw InvalidCurriculumException("최소한 하나의 섹션이 필요합니다")

        if (mutableSections.any { it.lessons.isEmpty() }) throw InvalidCurriculumException("수업이 없는 섹션은 허용되지 않습니다")
    }

    fun firstLesson(): Lesson? = allLessons().firstOrNull()

    fun nextLesson(lesson: Lesson): Lesson? {
        val lessons = allLessons()
        val index = lessons.indexOf(lesson)
        require(index >= 0) { "커리큘럼에 포함된 수업이 아닙니다" }

        return lessons.getOrNull(index + 1)
    }

    fun nextLesson(lessonId: Long): Lesson? {
        val lesson = allLessons().firstOrNull { it.id == lessonId }
            ?: throw IllegalArgumentException("레슨을 찾을 수 없습니다. ID: $lessonId")

        return nextLesson(lesson)
    }
}

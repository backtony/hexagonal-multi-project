package com.sample.hexagonal.splearn.domain.curriculum

import com.sample.hexagonal.splearn.domain.AbstractEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderColumn
import java.util.Collections

@Entity
class Section internal constructor(
    curriculum: Curriculum,
    title: String,
) : AbstractEntity() {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    val curriculum: Curriculum = curriculum

    @Column(length = 200)
    var title: String = title
        protected set

    @OneToMany(mappedBy = "section", cascade = [CascadeType.ALL])
    @OrderColumn(name = "lesson_order")
    private val mutableLessons: MutableList<Lesson> = mutableListOf()

    val lessons: List<Lesson>
        get() = Collections.unmodifiableList(mutableLessons)

    internal fun addLesson(title: String): Lesson = Lesson(this, title).also {
        mutableLessons.add(it)
        markUpdated()
    }

    internal fun updateTitle(title: String) {
        this.title = title
        markUpdated()
    }

    internal fun updateLessonTitle(lessonIndex: Int, title: String) {
        mutableLessons[lessonIndex].updateTitle(title)
    }

    internal fun removeLesson(lessonIndex: Int): Lesson = mutableLessons.removeAt(lessonIndex).also { markUpdated() }

    internal fun moveAllLessonsTo(target: Section, insertIndex: Int) {
        var index = insertIndex
        while (mutableLessons.isNotEmpty()) {
            target.addLesson(index++, mutableLessons.removeAt(0))
        }
        markUpdated()
    }

    internal fun addLesson(insertIndex: Int, lesson: Lesson) {
        lesson.moveTo(this)
        mutableLessons.add(insertIndex, lesson)
        markUpdated()
    }
}

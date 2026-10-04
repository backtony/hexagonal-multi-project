package com.sample.hexagonal.splearn.domain.curriculum

data class LessonContent(val title: String)

data class SectionContent(
    val title: String,
    val lessons: List<LessonContent>,
) {
    companion object {
        fun from(curriculum: Curriculum): List<SectionContent> =
            curriculum.sections.map { section -> SectionContent(section.title, section.lessons.map { LessonContent(it.title) }) }

        fun section(title: String, vararg lessons: LessonContent): SectionContent = SectionContent(title, lessons.toList())

        fun lesson(title: String): LessonContent = LessonContent(title)
    }
}

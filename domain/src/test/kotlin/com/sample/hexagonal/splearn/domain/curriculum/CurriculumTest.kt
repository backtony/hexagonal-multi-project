package com.sample.hexagonal.splearn.domain.curriculum

import com.sample.hexagonal.splearn.domain.EntityFixture
import com.sample.hexagonal.splearn.domain.course.CourseFixture
import com.sample.hexagonal.splearn.domain.curriculum.SectionContent.Companion.lesson
import com.sample.hexagonal.splearn.domain.curriculum.SectionContent.Companion.section
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe

class CurriculumTest :
    StringSpec({
        "create" {
            val course = CourseFixture.createCourse()

            Curriculum(course).course shouldBe course
        }

        "addSection" {
            val curriculum = CurriculumFixture.createCurriculum()

            val section = curriculum.addSection("Section 1")

            curriculum.sections shouldContainExactly listOf(section)
            curriculum.sections.map { it.title } shouldContainExactly listOf("Section 1")
        }

        "addSectionWithIndex" {
            val curriculum = CurriculumFixture.createCurriculum()
            val s1 = curriculum.addSection("S1")
            val s2 = curriculum.addSection("S2")

            curriculum.sections shouldContainExactly listOf(s1, s2)

            val s11 = curriculum.addSection(1, "S1_1")
            curriculum.sections shouldContainExactly listOf(s1, s11, s2)

            val s3 = curriculum.addSection(3, "S3")
            curriculum.sections shouldContainExactly listOf(s1, s11, s2, s3)

            shouldThrow<IndexOutOfBoundsException> { curriculum.addSection(5, "Fail") }
        }

        "addLesson" {
            val curriculum = CurriculumFixture.createCurriculum()
            val s0 = curriculum.addSection("S0")
            val s1 = curriculum.addSection("S1")

            val l0 = curriculum.addLesson(0, "L0")
            val l1 = curriculum.addLesson(0, "L1")
            s0.lessons shouldContainExactly listOf(l0, l1)

            val l2 = curriculum.addLesson(1, "L2")
            s1.lessons shouldContainExactly listOf(l2)
        }

        "updateSectionTitle" {
            val curriculum = CurriculumFixture.createCurriculum()
            val s0 = curriculum.addSection("S0")
            curriculum.addSection("S1")

            curriculum.updateSectionTitle(0, "S0 Updated")
            s0.title shouldBe "S0 Updated"

            curriculum.updateSectionTitle(1, "S1 Updated")
            curriculum.sections.map { it.title } shouldContainExactly listOf("S0 Updated", "S1 Updated")
        }

        "updateLessonTitle" {
            val curriculum = CurriculumFixture.createCurriculum()
            curriculum.addSection("S0")
            curriculum.addSection("S1")
            val l00 = curriculum.addLesson(0, "L0_0")
            curriculum.addLesson(0, "L0_1")
            val l1 = curriculum.addLesson(1, "L1")

            curriculum.updateLessonTitle(0, 0, "L0_0 Udt")
            l00.title shouldBe "L0_0 Udt"

            curriculum.updateLessonTitle(1, 0, "L1 Udt")
            l1.title shouldBe "L1 Udt"

            curriculum.sections.flatMap { it.lessons }.map { it.title } shouldContainExactly listOf("L0_0 Udt", "L0_1", "L1 Udt")
        }

        "removeLesson" {
            val curriculum = CurriculumFixture.createCurriculum()
            curriculum.addSection("S0")
            curriculum.addSection("S1")
            curriculum.addLesson(0, "L0")
            curriculum.addLesson(0, "L1")
            curriculum.addLesson(1, "L2")
            curriculum.addLesson(1, "L3")

            curriculum.allLessons().map { it.title } shouldContainExactly listOf("L0", "L1", "L2", "L3")

            curriculum.removeLesson(0, 0).deleted shouldBe true
            curriculum.allLessons().map { it.title } shouldContainExactly listOf("L1", "L2", "L3")

            curriculum.removeLesson(1, 1)
            curriculum.allLessons().map { it.title } shouldContainExactly listOf("L1", "L2")
        }

        "removeSection" {
            val curriculum = CurriculumFixture.createCurriculum()
            val s0 = curriculum.addSection("S0")
            val s1 = curriculum.addSection("S1")
            curriculum.addSection("S2")
            curriculum.addLesson(0, "L0")
            curriculum.addLesson(0, "L1")
            curriculum.addLesson(1, "L2")
            curriculum.addLesson(1, "L3")
            curriculum.addLesson(2, "L4")
            curriculum.addLesson(2, "L5")

            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"), lesson("L3")),
                section("S2", lesson("L4"), lesson("L5")),
            )

            // 섹션을 삭제하면 수업은 앞으로 섹션 수업의 뒤에 추가된다
            curriculum.removeSection(2)
            curriculum.sections shouldContainExactly listOf(s0, s1)
            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S0", lesson("L0"), lesson("L1")),
                section("S1", lesson("L2"), lesson("L3"), lesson("L4"), lesson("L5")),
            )

            // 단, 첫번째 섹션을 삭제하면 수업은 다음 섹션 앞부분으로 추가된다
            curriculum.removeSection(0).deleted shouldBe true
            curriculum.sections shouldContainExactly listOf(s1)
            curriculum.allLessons().none { it.deleted } shouldBe true
            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S1", lesson("L0"), lesson("L1"), lesson("L2"), lesson("L3"), lesson("L4"), lesson("L5")),
            )

            // 하나 남은 섹션은 삭제할 수 없다
            shouldThrow<IllegalStateException> { curriculum.removeSection(0) }
        }

        "moveLesson" {
            val curriculum = CurriculumFixture.createCurriculum()
            val s0 = curriculum.addSection("S0")
            val s1 = curriculum.addSection("S1")
            curriculum.addSection("S2")
            curriculum.addLesson(0, "L0")
            val l1 = curriculum.addLesson(0, "L1")
            curriculum.addLesson(0, "L2")
            curriculum.addLesson(1, "L3")
            curriculum.addLesson(1, "L4")
            curriculum.addLesson(2, "L5")
            val l6 = curriculum.addLesson(2, "L6")

            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S0", lesson("L0"), lesson("L1"), lesson("L2")),
                section("S1", lesson("L3"), lesson("L4")),
                section("S2", lesson("L5"), lesson("L6")),
            )

            curriculum.moveLesson(0, 0, 0, 1)
            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S0", lesson("L1"), lesson("L0"), lesson("L2")),
                section("S1", lesson("L3"), lesson("L4")),
                section("S2", lesson("L5"), lesson("L6")),
            )

            curriculum.moveLesson(0, 2, 0, 0)
            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S0", lesson("L2"), lesson("L1"), lesson("L0")),
                section("S1", lesson("L3"), lesson("L4")),
                section("S2", lesson("L5"), lesson("L6")),
            )

            curriculum.moveLesson(0, 1, 1, 2)
            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S0", lesson("L2"), lesson("L0")),
                section("S1", lesson("L3"), lesson("L4"), lesson("L1")),
                section("S2", lesson("L5"), lesson("L6")),
            )
            l1.section shouldBe s1

            curriculum.moveLesson(2, 1, 0, 1)
            SectionContent.from(curriculum) shouldContainExactly listOf(
                section("S0", lesson("L2"), lesson("L6"), lesson("L0")),
                section("S1", lesson("L3"), lesson("L4"), lesson("L1")),
                section("S2", lesson("L5")),
            )
            l6.section shouldBe s0
        }

        "validate" {
            val curriculum = CurriculumFixture.createCurriculum()

            // 최소한 하나의 섹션은 필요하다
            shouldThrow<InvalidCurriculumException> { curriculum.validate() }

            curriculum.addSection("S0")
            // 레슨이 없는 섹션은 검증 실패
            shouldThrow<InvalidCurriculumException> { curriculum.validate() }

            curriculum.addLesson(0, "L0")
            curriculum.validate()

            curriculum.addSection("S1")
            curriculum.addLesson(1, "L1")
            curriculum.validate()

            // 레슨이 없는 섹션은 검증 실패
            curriculum.removeLesson(1, 0)
            shouldThrow<InvalidCurriculumException> { curriculum.validate() }
        }

        "firstLesson" {
            val curriculum = CurriculumFixture.createCurriculum()
            curriculum.addSection("S0")
            curriculum.addSection("S1")

            curriculum.firstLesson().shouldBeNull()

            val l0 = curriculum.addLesson(0, "L0")
            curriculum.addLesson(0, "L1")
            curriculum.addLesson(0, "L2")
            curriculum.addLesson(1, "L3")
            curriculum.addLesson(1, "L4")

            curriculum.firstLesson() shouldBe l0
        }

        "next" {
            val curriculum = CurriculumFixture.createCurriculum()
            curriculum.addSection("S0")
            curriculum.addSection("S1")
            curriculum.addLesson(0, "L0")
            val l1 = curriculum.addLesson(0, "L1")
            val l2 = curriculum.addLesson(1, "L2")

            var lesson = curriculum.firstLesson()!!
            lesson = curriculum.nextLesson(lesson)!!
            lesson shouldBe l1

            lesson = curriculum.nextLesson(lesson)!!
            lesson shouldBe l2

            curriculum.nextLesson(lesson).shouldBeNull()
        }

        "nextWithId" {
            val curriculum = CurriculumFixture.createCurriculum()
            curriculum.addSection("S0")
            curriculum.addSection("S1")
            val l0 = curriculum.addLesson(0, "L0").also { EntityFixture.assignId(it, 10L) }
            val l1 = curriculum.addLesson(0, "L1").also { EntityFixture.assignId(it, 11L) }
            val l2 = curriculum.addLesson(1, "L2").also { EntityFixture.assignId(it, 12L) }

            var lesson = curriculum.nextLesson(l0.id!!)!!
            lesson shouldBe l1

            lesson = curriculum.nextLesson(lesson.id!!)!!
            lesson shouldBe l2

            curriculum.nextLesson(lesson.id!!).shouldBeNull()
        }

        "unmodifiableSectionsAndLessons" {
            val curriculum = CurriculumFixture.createCurriculum()
            curriculum.addSection("S0")
            curriculum.addLesson(0, "L0")

            shouldThrow<UnsupportedOperationException> {
                (curriculum.sections as MutableList<Section>).add(Section(curriculum, "Fail"))
            }

            val section = curriculum.sections.first()
            shouldThrow<UnsupportedOperationException> {
                (section.lessons as MutableList<Lesson>).add(Lesson(section, "Fail"))
            }
        }
    })

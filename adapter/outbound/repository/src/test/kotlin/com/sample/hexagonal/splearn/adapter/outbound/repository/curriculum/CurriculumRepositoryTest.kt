package com.sample.hexagonal.splearn.adapter.outbound.repository.curriculum

import com.sample.hexagonal.splearn.application.service.curriculum.port.outbound.CurriculumRepository
import com.sample.hexagonal.splearn.domain.curriculum.Curriculum
import com.sample.hexagonal.splearn.domain.curriculum.SectionContent
import com.sample.hexagonal.splearn.domain.curriculum.SectionContent.Companion.lesson
import com.sample.hexagonal.splearn.domain.curriculum.SectionContent.Companion.section
import com.sample.hexagonal.splearn.adapter.outbound.repository.RepositoryTest
import com.sample.hexagonal.splearn.adapter.outbound.repository.RepositoryTestDataPreparer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import jakarta.persistence.EntityManager

@RepositoryTest
class CurriculumRepositoryTest(
    curriculumRepository: CurriculumRepository,
    entityManager: EntityManager,
    preparer: RepositoryTestDataPreparer,
) : StringSpec({
    fun saveCurriculum(): Long {
        val curriculum = curriculumRepository.save(Curriculum(preparer.prepareCourse()))
        curriculum.addSection("S1")
        curriculum.addLesson(0, "L1")
        curriculum.addLesson(0, "L2")
        curriculum.addSection("S2")
        curriculum.addLesson(1, "L3")
        entityManager.flush()
        entityManager.clear()
        return curriculum.id!!
    }

    fun deletedOf(table: String, title: String): Boolean =
        entityManager.createNativeQuery("select deleted from $table where title = :title")
            .setParameter("title", title)
            .singleResult as Boolean

    "saveAndFindById" {
        val curriculumId = saveCurriculum()
        val statistics = preparer.prepareStatistics()

        val found = curriculumRepository.findById(curriculumId).shouldNotBeNull()
        statistics.prepareStatementCount shouldBe 1

        found.id shouldBe curriculumId
        SectionContent.from(found) shouldContainExactly listOf(
            section("S1", lesson("L1"), lesson("L2")),
            section("S2", lesson("L3")),
        )
        statistics.prepareStatementCount shouldBe 4
    }

    "saveAndFindWithSectionsById" {
        val curriculumId = saveCurriculum()
        val statistics = preparer.prepareStatistics()

        val found = curriculumRepository.findWithSectionsById(curriculumId).shouldNotBeNull()
        statistics.prepareStatementCount shouldBe 1

        found.id shouldBe curriculumId
        SectionContent.from(found) shouldContainExactly listOf(
            section("S1", lesson("L1"), lesson("L2")),
            section("S2", lesson("L3")),
        )
        statistics.prepareStatementCount shouldBe 1
    }

    "removeLesson" {
        val curriculumId = saveCurriculum()
        val curriculum = curriculumRepository.findWithSectionsById(curriculumId).shouldNotBeNull()

        curriculum.removeLesson(0, 0)
        entityManager.flush()
        entityManager.clear()

        SectionContent.from(curriculumRepository.findWithSectionsById(curriculumId)!!) shouldContainExactly listOf(
            section("S1", lesson("L2")),
            section("S2", lesson("L3")),
        )
        deletedOf("lesson", "L1") shouldBe true
    }

    "removeSection" {
        val curriculumId = saveCurriculum()
        val curriculum = curriculumRepository.findWithSectionsById(curriculumId).shouldNotBeNull()

        curriculum.removeSection(0)
        entityManager.flush()
        entityManager.clear()

        SectionContent.from(curriculumRepository.findWithSectionsById(curriculumId)!!) shouldContainExactly listOf(
            section("S2", lesson("L1"), lesson("L2"), lesson("L3")),
        )
        deletedOf("section", "S1") shouldBe true
    }

    "moveLesson" {
        val curriculumId = saveCurriculum()
        val curriculum = curriculumRepository.findWithSectionsById(curriculumId).shouldNotBeNull()

        curriculum.moveLesson(0, 0, 1, 1)
        entityManager.flush()
        entityManager.clear()

        SectionContent.from(curriculumRepository.findWithSectionsById(curriculumId)!!) shouldContainExactly listOf(
            section("S1", lesson("L2")),
            section("S2", lesson("L3"), lesson("L1")),
        )
    }
})

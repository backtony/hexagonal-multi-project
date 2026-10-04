package com.sample.hexagonal.splearn.application.service.curriculum

import com.sample.hexagonal.splearn.application.ApplicationServiceTest
import com.sample.hexagonal.splearn.application.ApplicationTestDataPreparer
import com.sample.hexagonal.splearn.application.service.curriculum.port.inbound.CurriculumCoordinator
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

@ApplicationServiceTest
class CurriculumCoordinatorTest(
    curriculumCoordinator: CurriculumCoordinator,
    preparer: ApplicationTestDataPreparer,
) : StringSpec({
    "createCurriculum" {
        val course = preparer.prepareCourse()

        val curriculum = curriculumCoordinator.createCurriculum(course.id!!)

        curriculum.id.shouldNotBeNull()
        curriculum.course.id shouldBe course.id
    }

    "createCurriculumReturnsExisting" {
        val course = preparer.prepareCourse()
        val created = curriculumCoordinator.createCurriculum(course.id!!)

        curriculumCoordinator.createCurriculum(course.id!!).id shouldBe created.id
    }
})

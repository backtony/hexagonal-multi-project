package com.sample.hexagonal.splearn.adapter.inbound.event.course

import com.sample.hexagonal.splearn.application.service.curriculum.port.inbound.CurriculumCoordinator
import com.sample.hexagonal.splearn.domain.course.event.CourseCreatedEvent
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk
import io.mockk.verify

class CourseDomainEventListenerTest :
    StringSpec({
        "강의 생성 이벤트를 받으면 커리큘럼을 생성한다" {
            val curriculumCoordinator = mockk<CurriculumCoordinator>(relaxed = true)

            CourseDomainEventListener(curriculumCoordinator).handle(CourseCreatedEvent(1L))

            verify { curriculumCoordinator.createCurriculum(1L) }
        }
    })

package com.sample.hexagonal.splearn.adapter.inbound.event.course

import com.sample.hexagonal.splearn.application.service.curriculum.port.inbound.CurriculumCoordinator
import com.sample.hexagonal.splearn.domain.course.event.CourseCreatedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * 강의를 생성한 트랜잭션 안에서 동기로 커리큘럼을 만든다. 실패하면 강의 생성도 함께 롤백된다.
 */
@Component
class CourseDomainEventListener(private val curriculumCoordinator: CurriculumCoordinator) {
    @EventListener
    fun handle(event: CourseCreatedEvent) {
        curriculumCoordinator.createCurriculum(event.courseId)
    }
}

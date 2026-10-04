package com.sample.hexagonal.splearn.application.service.course.spi

import com.sample.hexagonal.splearn.domain.curriculum.InvalidCurriculumException

/**
 * 강의를 심사 요청·공개하기 전에 커리큘럼이 유효한지 확인한다.
 *
 * SPI: course 가 정의하고 curriculum 이 구현한다.
 *
 * 순환 방지
 * - 기존 의존: CurriculumModifyService → CourseFinder (커리큘럼 생성 시 강의 조회)
 * - CourseValidator → CurriculumFinder 로 직접 의존하면 course ↔ curriculum 순환이 생긴다.
 *
 * DIP
 * - 호출: CourseValidator → CurriculumValidator ← CurriculumValidationService
 * - 패키지 의존: curriculum → course 한 방향만 유지된다.
 */
interface CurriculumValidator {
    /**
     * 커리큘럼이 없거나, 섹션이 없거나, 수업이 없는 섹션이 있으면 [InvalidCurriculumException] 을 던진다.
     */
    @Throws(InvalidCurriculumException::class)
    fun validate(courseId: Long)
}

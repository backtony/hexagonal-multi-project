package com.sample.hexagonal.splearn.application.service.instructor

import com.sample.hexagonal.common.utils.exception.getOrThrowNotFound
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.DuplicateInstructorApplicationException
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplication
import com.sample.hexagonal.splearn.application.service.instructor.port.inbound.InstructorApplyRequest
import com.sample.hexagonal.splearn.application.service.member.port.inbound.MemberFinder
import com.sample.hexagonal.splearn.application.service.instructor.port.outbound.InstructorRepository
import com.sample.hexagonal.splearn.domain.instructor.Instructor
import com.sample.hexagonal.splearn.domain.member.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated

@Service
@Transactional
@Validated
class InstructorModifyService(
    private val instructorRepository: InstructorRepository,
    private val memberFinder: MemberFinder,
) : InstructorApplication {
    override fun apply(applyRequest: InstructorApplyRequest): Instructor {
        val member = memberFinder.find(applyRequest.memberId)

        checkDuplicateApplication(member)

        return instructorRepository.save(Instructor.apply(member))
    }

    override fun approve(instructorId: Long): Instructor {
        val instructor = instructorRepository.findById(instructorId).getOrThrowNotFound(instructorId)

        instructor.approve()

        return instructorRepository.save(instructor)
    }

    override fun reject(instructorId: Long): Instructor {
        val instructor = instructorRepository.findById(instructorId).getOrThrowNotFound(instructorId)

        instructor.reject()

        return instructorRepository.save(instructor)
    }

    private fun checkDuplicateApplication(member: Member) {
        if (instructorRepository.findByMemberId(member.id!!) != null) {
            throw DuplicateInstructorApplicationException("회원은 중복해서 강사 신청을 할 수 없습니다")
        }
    }
}

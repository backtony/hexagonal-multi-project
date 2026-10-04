package com.sample.hexagonal.splearn.domain.member

import com.sample.hexagonal.splearn.domain.AbstractEntity
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(
    name = "member_detail",
    uniqueConstraints = [UniqueConstraint(name = "UK_MEMBER_DETAIL_PROFILE_ADDRESS", columnNames = ["profile_address"])],
)
class MemberDetail protected constructor() : AbstractEntity() {
    @Embedded
    var profile: Profile? = null
        protected set

    @Column(columnDefinition = "TEXT")
    var introduction: String? = null
        protected set

    var activatedAt: LocalDateTime? = null
        protected set

    var deactivatedAt: LocalDateTime? = null
        protected set

    internal fun activate() {
        require(activatedAt == null) { "이미 activatedAt은 설정되었습니다" }

        activatedAt = LocalDateTime.now()
        markUpdated()
    }

    internal fun deactivate() {
        require(deactivatedAt == null) { "이미 deactivatedAt은 설정되었습니다" }

        deactivatedAt = LocalDateTime.now()
        markUpdated()
    }

    internal fun updateInfo(updateRequest: MemberInfoUpdate) {
        profile = updateRequest.profileAddress.takeIf { it.isNotEmpty() }?.let(::Profile)
        introduction = updateRequest.introduction
        markUpdated()
    }

    companion object {
        internal fun create(): MemberDetail = MemberDetail()
    }
}

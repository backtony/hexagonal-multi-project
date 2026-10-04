package com.sample.hexagonal.splearn.domain.member

import com.sample.hexagonal.splearn.domain.AbstractEntity
import com.sample.hexagonal.splearn.domain.AggregateRoot
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@AggregateRoot
@Entity
@Table(
    name = "member",
    uniqueConstraints = [
        UniqueConstraint(name = "UK_MEMBER_EMAIL_ADDRESS", columnNames = ["email_address"]),
        UniqueConstraint(name = "UK_MEMBER_DETAIL_ID", columnNames = ["detail_id"]),
    ],
)
class Member protected constructor(
    email: Email,
    nickname: String,
    passwordHash: String,
    detail: MemberDetail,
) : AbstractEntity() {
    @Embedded
    val email: Email = email

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val detail: MemberDetail = detail

    @Column(nullable = false, length = 100)
    var nickname: String = nickname
        protected set

    @Column(nullable = false, length = 200)
    var passwordHash: String = passwordHash
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var status: MemberStatus = MemberStatus.PENDING
        protected set

    fun activate() {
        check(status == MemberStatus.PENDING) { "PENDING 상태가 아닙니다" }

        status = MemberStatus.ACTIVE
        detail.activate()
        markUpdated()
    }

    fun deactivate() {
        check(status == MemberStatus.ACTIVE) { "ACTIVE 상태가 아닙니다" }

        status = MemberStatus.DEACTIVATED
        detail.deactivate()
        markUpdated()
    }

    fun verifyPassword(password: String, passwordEncoder: PasswordEncoder): Boolean = passwordEncoder.matches(password, passwordHash)

    fun updateInfo(updateRequest: MemberInfoUpdate) {
        check(status == MemberStatus.ACTIVE) { "등록 완료 상태가 아니면 정보를 수정할 수 없습니다" }

        nickname = updateRequest.nickname
        detail.updateInfo(updateRequest)
        markUpdated()
    }

    fun changePassword(password: String, passwordEncoder: PasswordEncoder) {
        passwordHash = passwordEncoder.encode(password)
        markUpdated()
    }

    fun isActive(): Boolean = status == MemberStatus.ACTIVE

    fun ensureActive() {
        check(status == MemberStatus.ACTIVE) { "회원의 상태가 ACTIVE가 아닙니다" }
    }

    companion object {
        fun register(registerInfo: MemberRegisterInfo, passwordEncoder: PasswordEncoder): Member = Member(
            email = Email(registerInfo.email),
            nickname = registerInfo.nickname,
            passwordHash = passwordEncoder.encode(registerInfo.password),
            detail = MemberDetail.create(),
        )
    }
}

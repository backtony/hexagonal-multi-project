package com.sample.hexagonal.splearn.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Email(
    @Column(name = "email_address", nullable = false, length = 150)
    val address: String,
) {
    init {
        require(EMAIL_PATTERN.matches(address)) { "이메일 형식이 바르지 않습니다: $address" }
    }

    companion object {
        private val EMAIL_PATTERN =
            Regex("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    }
}

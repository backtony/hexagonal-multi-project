package com.sample.hexagonal.splearn.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class Profile(
    @Column(name = "profile_address", length = 20)
    val address: String,
) {
    init {
        require(address.isEmpty() || PROFILE_ADDRESS_PATTERN.matches(address)) { "프로필 주소 형식이 바르지 않습니다: $address" }
        require(address.length <= 15) { "프로필 주소는 최대 15자리를 넘을 수 없습니다" }
    }

    fun url(): String = "@$address"

    companion object {
        private val PROFILE_ADDRESS_PATTERN = Regex("[a-z0-9]+")
    }
}

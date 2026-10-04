package com.sample.hexagonal.splearn.domain.member

data class MemberInfoUpdate(
    val nickname: String,
    val profileAddress: String,
    val introduction: String,
)

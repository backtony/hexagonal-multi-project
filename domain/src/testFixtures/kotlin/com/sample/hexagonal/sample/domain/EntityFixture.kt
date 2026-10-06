package com.sample.hexagonal.sample.domain

object EntityFixture {
    fun assignId(entity: AbstractEntity, id: Long) {
        AbstractEntity::class.java.getDeclaredField("id").apply { isAccessible = true }.set(entity, id)
    }
}

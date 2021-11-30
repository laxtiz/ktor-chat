package com.laxtiz.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MemberEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MemberEntity>(MemberTable) {
        fun findByUsername(username: String) = find { MemberTable.username eq username }.firstOrNull()
    }

    var username by MemberTable.username
    var password by MemberTable.password
    var createdTime by MemberTable.createdTime

    val messages by MessageEntity referrersOn MessageTable.from
}
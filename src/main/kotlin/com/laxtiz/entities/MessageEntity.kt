package com.laxtiz.entities

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MessageEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<MessageEntity>(MessageTable)

    var from by MemberEntity referencedOn MessageTable.from
    var text by MessageTable.text
    var createdTime by MessageTable.createdTime
}
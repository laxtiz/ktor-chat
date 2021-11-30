package com.laxtiz.entities

import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object MessageTable : LongIdTable("message") {
    val from = reference("from", MemberTable)
    val text = varchar("text", 128)
    val createdTime = datetime("created_time").clientDefault { java.time.LocalDateTime.now().toKotlinLocalDateTime() }
}


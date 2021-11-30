package com.laxtiz.entities

import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object MemberTable : LongIdTable("member") {
    val username = varchar("username", length = 16)
    val password = varchar("password", length = 64)
    val createdTime = datetime("created_time").clientDefault { java.time.LocalDateTime.now().toKotlinLocalDateTime() }
}


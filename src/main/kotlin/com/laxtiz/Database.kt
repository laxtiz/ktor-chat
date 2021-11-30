package com.laxtiz

import com.laxtiz.entities.MemberTable
import com.laxtiz.entities.MessageTable
import io.ktor.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Database.Companion.init(application: Application): Database {
    val config = application.environment.config

    val url = config.property("database.url").getString()
    val user = config.propertyOrNull("database.user")?.getString() ?: ""
    val password = config.propertyOrNull("database.password")?.getString() ?: ""

    return connect(url, user = user, password = password).also {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(MemberTable, MessageTable)
        }
    }
}
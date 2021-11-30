package com.laxtiz.services

import com.laxtiz.entities.MemberEntity
import com.laxtiz.utils.EncryptUtils
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class AuthService(private val database: Database) {
    suspend fun validate(username: String, password: String): Boolean {
        val member = newSuspendedTransaction(db = database) {
            MemberEntity.findByUsername(username)
        } ?: return false

        return EncryptUtils.checkPassword(password, member.password)
    }
}
package com.laxtiz.services

import com.laxtiz.entities.MemberEntity
import com.laxtiz.models.Member
import com.laxtiz.utils.EncryptUtils
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class MemberService(private val database: Database) {
    suspend fun createMember(username: String, password: String): Member {
        return newSuspendedTransaction(db = database) {
            MemberEntity.new {
                this.username = username
                this.password = EncryptUtils.hashPassword(password)
            }
        }.toModel()
    }

    private fun MemberEntity.toModel() = Member(id.value, username)
}
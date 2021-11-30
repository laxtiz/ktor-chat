package com.laxtiz.services

import com.laxtiz.entities.MemberEntity
import com.laxtiz.entities.MessageEntity
import com.laxtiz.entities.MessageTable
import com.laxtiz.models.Message
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ChatService(private val database: Database) {
    private val members: HashMap<String, WebSocketSession> = HashMap()

    fun join(username: String, session: WebSocketSession) {
        members[username] = session
    }

    fun leave(username: String) {
        members.remove(username)
    }

    suspend fun send(message: Message) {
        val data = Json.encodeToString(message)
        for (session in members.values) {
            session.outgoing.send(Frame.Text(data))
        }
        newSuspendedTransaction {
            val member = MemberEntity.findByUsername(message.username)!!
            MessageEntity.new {
                from = member
                text = message.text
            }

        }
    }
}
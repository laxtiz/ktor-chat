package com.laxtiz.controllers

import com.laxtiz.exceptions.AuthenticationException
import com.laxtiz.models.Message
import com.laxtiz.services.ChatService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class ChatController(application: Application) : AbstractDIController(application) {
    private val chatService by instance<ChatService>()
    override fun Route.getRoutes() {
        authenticate {
            webSocket("/tang/ws") {
                val principal = call.principal<JWTPrincipal>()!!
                val username = principal["member"] ?: throw AuthenticationException()
                chatService.join(username, this)
                outgoing.send(Frame.Text("you are welcome."))

                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val content = frame.readText()
                            val message = Json.decodeFromString(Message.serializer(), content)
                            message.username = username
                            chatService.send(message)
                        }
                        is Frame.Close -> {
                            chatService.leave(username)
                            break
                        }
                        else -> {}
                    }
                }
            }
        }
    }

}
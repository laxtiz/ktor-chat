package com.laxtiz

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.laxtiz.controllers.AuthController
import com.laxtiz.controllers.ChatController
import com.laxtiz.exceptions.AuthenticationException
import com.laxtiz.exceptions.AuthorizationException
import com.laxtiz.services.AuthService
import com.laxtiz.services.ChatService
import com.laxtiz.services.MemberService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.kodein.di.ktor.controller.controller
import org.kodein.di.ktor.di
import org.kodein.di.singleton
import org.slf4j.event.Level
import java.time.Duration

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
        header("X-Author", "Laxtiz")
    }

    install(AutoHeadResponse)

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = environment.developmentMode
            encodeDefaults = true
        })
    }

    install(StatusPages) {
        exception<AuthenticationException> { cause ->
            call.respond(HttpStatusCode.Unauthorized)
        }

        exception<AuthorizationException> { cause ->
            call.respond(HttpStatusCode.Forbidden)
        }
    }


    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    di {
        bind { singleton { Database.init(instance()) } }
        bind { singleton { JwtConfig.init(instance()) } }
        bind { singleton { MemberService(instance()) } }
        bind { singleton { AuthService(instance()) } }
        bind { singleton { ChatService(instance()) } }
    }

    authentication {
        jwt {
            val config by closestDI().instance<JwtConfig>()
            realm = config.realm
            verifier(
                JWT.require(Algorithm.HMAC256(config.secret))
                    .withIssuer(config.issuer)
                    .withAudience(config.audience)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
            }
        }
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        controller { AuthController(instance()) }
        controller { ChatController(instance()) }
    }
}

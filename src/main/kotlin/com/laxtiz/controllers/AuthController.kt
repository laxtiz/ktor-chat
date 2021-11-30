package com.laxtiz.controllers

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.laxtiz.JwtConfig
import com.laxtiz.exceptions.AuthenticationException
import com.laxtiz.models.LoginFrom
import com.laxtiz.models.RegisterFrom
import com.laxtiz.services.AuthService
import com.laxtiz.services.MemberService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.kodein.di.Instance
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController
import java.time.LocalDateTime
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class AuthController(application: Application) : AbstractDIController(application) {
    private val authService by instance<AuthService>()
    private val memberService by instance<MemberService>()
    private val jwtConfig by instance<JwtConfig>()

    override fun Route.getRoutes() {
        route("/auth") {
            post<LoginFrom>("/login") { form ->
                if (!authService.validate(form.username, form.password)) throw AuthenticationException()
                val now = Clock.System.now()
                val exp = if (form.rememberMe) now + 7.days else now + 1.hours
                val token = JWT.create()
                    .withIssuer(jwtConfig.issuer)
                    .withAudience(jwtConfig.audience)
                    .withClaim("member", form.username)
                    .withExpiresAt(Date.from(exp.toJavaInstant()))
                    .sign(Algorithm.HMAC256(jwtConfig.secret))
                call.respond(hashMapOf("token" to token))
            }

            post<RegisterFrom>("/reg") { form ->
                val member = memberService.createMember(form.username, form.password)
                call.respond(HttpStatusCode.Created, member)
            }
        }
    }
}
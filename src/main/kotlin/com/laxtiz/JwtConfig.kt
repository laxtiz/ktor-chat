package com.laxtiz

import io.ktor.application.*

data class JwtConfig(val secret: String, val issuer: String, val audience: String, val realm: String) {
    companion object {
        fun init(application: Application): JwtConfig {
            val config = application.environment.config

            val secret = config.property("jwt.secret").getString()
            val issuer = config.property("jwt.issuer").getString()
            val audience = config.property("jwt.audience").getString()
            val realm = config.property("jwt.realm").getString()

            return JwtConfig(secret, issuer, audience, realm)
        }
    }
}
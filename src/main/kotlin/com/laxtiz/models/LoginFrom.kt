package com.laxtiz.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginFrom(val username: String, val password: String, val rememberMe: Boolean = true)
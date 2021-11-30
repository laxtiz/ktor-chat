package com.laxtiz.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterFrom(val username: String, val password: String)
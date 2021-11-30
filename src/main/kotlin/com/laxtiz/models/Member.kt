package com.laxtiz.models

import kotlinx.serialization.Serializable

@Serializable
data class Member(val id: Long, val username: String)
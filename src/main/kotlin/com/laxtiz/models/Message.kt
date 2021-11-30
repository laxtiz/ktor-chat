package com.laxtiz.models

import kotlinx.serialization.Serializable

@Serializable
data class Message(var username: String = "", val text: String)
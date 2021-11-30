package com.laxtiz.utils

import org.mindrot.jbcrypt.BCrypt

object EncryptUtils {
    fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }
}
package com.example.models

import java.io.Serializable
import io.ktor.auth.Principal

data class User(
    val userId: Int,
    val email: String,
    val displayName: String,
    val passwordHash: String
) : Serializable, Principal
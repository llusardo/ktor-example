package com.example.controllers

import com.example.exceptions.InvalidCredentialsException
import com.example.jwt.SimpleJWT
import com.example.model.LoginRegister
import com.example.model.PostSnippet
import com.example.model.Snippet
import com.example.model.User
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import java.util.*

val users = Collections.synchronizedMap(
    listOf(User("test", "test"))
        .associateBy { it.name }
        .toMutableMap()
)

fun Routing.authRoutes(simpleJwt: SimpleJWT) {
    post("/login-register") {
        val post = call.receive<LoginRegister>()
        val user = users.getOrPut(post.user) { User(post.user, post.password) }
        if (user.password != post.password) throw InvalidCredentialsException("Invalid credentials")
        call.respond(mapOf("token" to simpleJwt.sign(user.name)))
    }
}
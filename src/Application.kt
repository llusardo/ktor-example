package com.example

import com.example.controllers.authRoutes
import com.example.controllers.snippetRoutes
import com.example.exceptions.InvalidCredentialsException
import com.example.jwt.SimpleJWT
import com.example.model.PostSnippet
import com.example.model.Snippet
import com.example.model.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import java.util.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        jackson {
        }
    }
    install(StatusPages) {
        exception<InvalidCredentialsException> { exception ->
            call.respond(HttpStatusCode.Unauthorized, mapOf("error" to (exception.message ?: "")))
        }
    }
    val simpleJwt = SimpleJWT("my-super-secret-for-jwt")
    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }
        }
    }
    routing {
        snippetRoutes()
        authRoutes(simpleJwt)
    }
}

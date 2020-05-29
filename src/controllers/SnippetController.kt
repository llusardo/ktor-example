package com.example.controllers

import com.example.model.PostSnippet
import com.example.model.Snippet
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

val snippets = Collections.synchronizedList(mutableListOf(
    Snippet(user = "test", text = "hello"),
    Snippet(user = "test", text = "world")
))

fun Routing.snippetRoutes() {
    route("/snippets") {
        get {
            call.respond(kotlin.collections.mapOf("snippets" to kotlin.synchronized(snippets) { snippets.toList() }))
        }
        authenticate {
            post {
                val post = call.receive<PostSnippet>()
                val principal = call.principal<UserIdPrincipal>() ?: kotlin.error("No principal")
                snippets += com.example.model.Snippet(principal.name, post.snippet.text)
                call.respond(kotlin.collections.mapOf("OK" to true))
            }
        }
    }
}
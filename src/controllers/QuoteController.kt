package com.example.controllers

import com.example.clients.http.impl.QuoteApiClientImpl
import com.example.configuration.Config
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route

fun Routing.quoteRoutes() {
    route("/quote") {
        get {

            val config = Config().get().getConfig("ktor.quoteService")
           // val configObject = Config().get().getObject("ktor.quoteService")
            val quoteApiClientImpl = QuoteApiClientImpl(config)
            //Config().get<com.typesafe.config.Config>("ktor.quoteService")
            quoteApiClientImpl.getQuote()
            call.respond(kotlin.collections.mapOf("snippets" to kotlin.synchronized(snippets) { snippets.toList() }))
        }

    }
}
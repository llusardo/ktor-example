package com.example

import com.example.auth.JwtService
import com.example.auth.MySession
import com.example.auth.hash
import com.example.repository.DatabaseFactory
import com.example.repository.TodoRepository
import com.example.routes.users
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.jwt
import io.ktor.gson.*
import io.ktor.features.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

const val API_VERSION = "/v1"

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(Locations) {
    }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }

    }

    install(Authentication) {
        // 1
        DatabaseFactory.init()
        val db = TodoRepository()

        // 2
        val jwtService = JwtService()
        val hashFunction = { s: String -> hash(s) }

        jwt("jwt") { //1
            verifier(jwtService.verifier) // 2
            realm = "Todo Server"
            validate { // 3
                val payload = it.payload
                val claim = payload.getClaim("id")
                val claimString = claim.asInt()
                val user = db.findUser(claimString) // 4
                user
            }
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        val db = TodoRepository()
        val jwtService = JwtService()
        val hashFunction = { s: String -> hash(s) }

        users(db, jwtService, hashFunction)
    }
}


package com.example.clients.http.impl

import com.example.clients.http.HttpClient
import com.example.clients.http.QuoteApiClient
import com.example.configuration.Config
//import com.example.configuration.get
import com.example.model.Quote
import com.example.model.QuoteValue
import com.typesafe.config.ConfigObject
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

open class QuoteApiClientImpl(config: com.typesafe.config.Config) : HttpClient(config.getConfig("client.http")), QuoteApiClient {

    //protected val serviceConfig: ConfigObject = config.get().getObject("ktor.quoteService")
    //private val host = serviceConfig["host"]
    private val host = config.getString("host")

     suspend fun getQuoteParallel() = coroutineScope<Unit> {
        //val client = HttpClient()

        // Start two requests asynchronously.
        val firstRequest = async { get().get<ByteArray>("$host/api/random") }
        val secondRequest = async { get().get<ByteArray>("$host/api/random") }

        // Get the request contents without blocking threads, but suspending the function until both
        // requests are done.
        val bytes1 = firstRequest.await() // Suspension point.
        val bytes2 = secondRequest.await() // Suspension point.

        //client.close()
    }

    override suspend fun getQuote(): Quote {
        getQuoteParallel()
        return Quote(type = "type", value = QuoteValue(id = 1, quote = "quote"))
    }

}
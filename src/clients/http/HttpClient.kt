package com.example.clients.http

import com.example.configuration.Config
//import com.example.configuration.get
import com.typesafe.config.ConfigObject
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature

open class HttpClient (config: com.typesafe.config.Config) {

    private val clientConfig = config
    private val client = createClient()

    fun get(): HttpClient {
        return client
    }

    private fun createClient(): HttpClient {
        return HttpClient(Apache) {
            engine {
                socketTimeout = clientConfig.getInt("socketTimeout")
                connectTimeout = clientConfig.getInt("connectTimeout")
                connectionRequestTimeout = clientConfig.getInt("connectionRequestTimeout")
                customizeClient {
                    setMaxConnTotal(clientConfig.getInt("apache.maxConnTotal"))
                    setMaxConnPerRoute(clientConfig.getInt("apache.maxConnPerRoute"))
                }
            }
            install(JsonFeature) {
                serializer = JacksonSerializer()
            }
        }
    }
}
package com.example.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.port
import kotlinx.coroutines.runBlocking
import java.io.File

class VaultConfig {

    fun getSecrets(): Map<String, Any> {
        return runBlocking {
            val client = getClient()
            return@runBlocking try {
                val token = getVaultToken()
                val env = Environment.getEnvironment()
                val url = "https://$VAULT_HOST/$VAULT_VERSION/secret/@project_name@/$env"
                client.get<Vault>(url) {
                    header("X-Vault-Token", token)
                    port = VAULT_PORT
                }.data
            } catch (ex: ClientRequestException) {
                emptyMap<String, Any>()
            } finally {
                client.close()
            }
        }
    }

    private fun getVaultToken(): String {
        val path = "${System.getenv("HOME")}/.vault_token"
        return File(path).readText(Charsets.UTF_8).trim()
    }

    private fun getClient(): HttpClient {
        return HttpClient {
            install(JsonFeature) {
                serializer = JacksonSerializer {
                    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                }
            }
        }
    }

    companion object {
        private const val VAULT_PORT: Int = 443
        private const val VAULT_HOST: String = ""
        private const val VAULT_VERSION: String = "v1"
    }

    private data class Vault(
            val data: Map<String, Any>
    )
}
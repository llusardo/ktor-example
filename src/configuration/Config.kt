package com.example.configuration

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

class Config {

    private val stores = getStores()

    fun get(): Config {
        return stores
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(path: String): T {
        return stores.getAnyRef(path) as T
    }

    private fun getStores(): Config {
        //val vaultConfig = ConfigFactory.parseMap(VaultConfig().getSecrets()).resolve()
        return ConfigFactory.parseResources("application.${Environment.getEnvironment()}.conf")
                //.withFallback(vaultConfig)
                .resolve()
    }
}

/*@Suppress("UNCHECKED_CAST")
fun <T> Config.get(path: String): T {
    return this.getAnyRef(path) as T
}*/
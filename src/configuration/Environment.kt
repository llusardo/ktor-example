package com.example.configuration

object Environment {

    private const val ENV_VARIABLE = "env"
    private const val DEVELOPMENT = "dev"
    private const val STAGING = "stg"
    private const val PRODUCTION = "live"

    fun isDevelopment(): Boolean {
        return DEVELOPMENT.contains(getEnvironment())
    }

    fun isStaging(): Boolean {
        return STAGING.contains(getEnvironment())
    }

    fun isProduction(): Boolean {
        return PRODUCTION.contains(getEnvironment())
    }

    fun getEnvironment(): String {
        val envArg = System.getProperty(ENV_VARIABLE)
        if (envArg.isNullOrEmpty()) {
            System.setProperty(ENV_VARIABLE, DEVELOPMENT)
        }
        return System.getProperty(ENV_VARIABLE)
    }
}
package com.m4ykey.stos.core.network

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object NetworkClient {

    private const val API_VERSION = "2.3"
    private const val API_KEY = ""
    private const val BASE_HOST = "api.stackexchange.com"
    private const val DEFAULT_SITE = "stackoverflow"

    fun create(engine : HttpClientEngine) : HttpClient {
        return HttpClient(engine) {
            configureLogging()
            configureDefaultRequest()
            configureTimeout()
            configureRetries()
            configureValidator()
            configureContentNegotiation()
        }
    }

    private fun HttpClientConfig<*>.configureContentNegotiation() {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    private fun HttpClientConfig<*>.configureValidator() {
        expectSuccess = false
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.value >= 400) {
                    throw ResponseException(response, "Http ${response.status.value}: ${response.status.description}")
                }
            }
        }
    }

    private fun HttpClientConfig<*>.configureTimeout() {
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000
            socketTimeoutMillis = 15_000
            connectTimeoutMillis = 10_000
        }
    }

    private fun HttpClientConfig<*>.configureDefaultRequest() {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_HOST
                path(API_VERSION)
                setParameters(
                    "key" to API_KEY,
                    "site" to DEFAULT_SITE
                )
            }
            contentType(ContentType.Application.Json)
        }
    }

    private fun HttpClientConfig<*>.configureRetries() {
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }
    }

    private fun HttpClientConfig<*>.configureLogging() {
        install(Logging) {
            level = LogLevel.BODY
            logger = Logger.DEFAULT
        }
    }
}
package com.stacklabs.micronaut.workshop.agency

import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


@MicronautTest
class CarsControllerTest(private val embeddedServer: EmbeddedServer) {

    @Test
    fun testHelloMicronaut() =
        embeddedServer.applicationContext.createBean(RxHttpClient::class.java, embeddedServer.url)
            .use { client -> assertEquals("Hello Micronaut!", client.toBlocking().exchange("/cars", String::class.java).body()) }
}
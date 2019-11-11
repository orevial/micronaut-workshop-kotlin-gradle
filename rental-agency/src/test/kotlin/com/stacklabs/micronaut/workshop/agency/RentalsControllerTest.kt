package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.agency.domain.Car
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import io.micronaut.context.ApplicationContext
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RentalsControllersTest {

    lateinit var server: EmbeddedServer
    lateinit var client: HttpClient
    lateinit var repository: CarRepository

    @BeforeEach
    fun setup() {
        server = ApplicationContext
            .build()
            .run(EmbeddedServer::class.java)
        client = server.applicationContext.createBean(HttpClient::class.java, server.url)
        repository = server.applicationContext.getBean(CarRepository::class.java)
        repository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        repository.deleteAll()
        server.close()
    }

    @Test
    fun getPrice() {
        // Given
        val carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().id

        // When
        val response = client.toBlocking()
            .retrieve("/cars/$carId/rentals/_price?nbDays=5&driverAge=35&kilometers=500&insurance=true")

        // Then
        assertThat(response).isEqualTo("This will cost you: 10.0€")
    }

    @Test
    fun getPrice_forUnknownCar_return404() {
        // Given
        val carId = "00000000-0000-0000-0000-000000000000"

        // When + Then
        assertThatThrownBy {
            client.toBlocking()
                .retrieve("/cars/$carId/rentals/_price?nbDays=5&driverAge=35&kilometers=500&insurance=true")
        }.isInstanceOf(Exception::class.java)
    }

    @Test
    fun getPrice_withoutParameters_return404() {
        // Given
        val carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().id

        // When + Then
        assertThatThrownBy {
            client.toBlocking()
                .retrieve("/cars/$carId/rentals/_price?nbDays=5&insurance=true")
        }.isInstanceOf(Exception::class.java)
    }

    private fun createCar(registration: String, brand: String, model: String) = Car(
        registration = registration,
        brand = brand,
        model = model,
        category = CarCategory.COMPACT_CAR
    )
}
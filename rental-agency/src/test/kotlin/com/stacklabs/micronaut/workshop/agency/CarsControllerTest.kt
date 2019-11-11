package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.agency.domain.Car
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import java.util.*

class CarsControllerTest {

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
    fun addCar() {
        // Add a car
        val response = addCar(createCar("FIRST-CAR", "Lada", "Model1"))
        assertThat(response.status).isEqualTo(HttpStatus.CREATED)
        JSONAssert.assertEquals(
            """
                {
                    "registration":"FIRST-CAR",
                    "brand": "Lada",
                    "model": "Model1",
                    "category": "COMPACT_CAR"
                }
            """,
            response.body.get(),
            JSONCompareMode.LENIENT
        )
    }

    @Test
    fun addCar_shouldThrowAnException_whenCarAlreadyExist() {
        // Given
        repository.save(createCar("FIRST-CAR", "Lada", "Model1"))

        // When + Then
        assertThatThrownBy { addCar(createCar("FIRST-CAR", "Even other brand should fail", "Another model")) }
            .hasMessageContaining("Duplicate record")
    }

    @Test
    fun updateCar() {
        // Given
        val carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().id

        // When
        updateCar(carId.toString(), createCar("FIRST-CAR", "Lada", "Model2"))

        // Then
        assertThat(repository.findById(carId!!).get().model).isEqualTo("Model2")
    }

    @Test
    fun findAllCars_returnEmptyList_onStartup() {
        // Test initial empty list
        val (status, body) = getCars()
        assertThat(status).isEqualTo(HttpStatus.OK)
        assertThat(body).isEqualTo("[]")
    }

    @Test
    fun findAllCars() {
        // Given
        repository.save(createCar("FIRST-CAR", "Lada", "Model1"))
        repository.save(createCar("SECOND-CAR", "Renault", "Capture"))

        // When
        val body = getCars().second

        // Then
        JSONAssert.assertEquals(
            """[
                {
                    "registration":"FIRST-CAR",
                    "brand": "Lada",
                    "model": "Model1",
                    "category": "COMPACT_CAR"
                },
                {
                    "registration":"SECOND-CAR",
                    "brand": "Renault",
                    "model": "Capture",
                    "category": "COMPACT_CAR"
                }
                ]
            """,
            body,
            JSONCompareMode.LENIENT
        )
    }

    @Test
    fun findCarById() {
        // Given
        val carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().id

        // When
        val body = getCars(id = carId).second

        // Then
        JSONAssert.assertEquals(
            """
                {
                    "registration":"FIRST-CAR",
                    "brand": "Lada",
                    "model": "Model1",
                    "category": "COMPACT_CAR"
                }
            """,
            body,
            JSONCompareMode.LENIENT
        )
    }

    @Test
    fun findCarById_withUnknownId_shouldReturn404() {
        assertThatThrownBy { getCars(id = UUID.fromString("00000000-0000-0000-0000-000000000001")) }
            .hasMessage("Page Not Found")
    }

    @Test
    fun findCarByRegistration() {
        // Given
        repository.save(createCar("FIRST-CAR", "Lada", "Model1"))

        // When
        val body = getCars(registration = "FIRST-CAR").second

        // Then
        JSONAssert.assertEquals(
            """[
                {
                    "registration":"FIRST-CAR",
                    "brand": "Lada",
                    "model": "Model1",
                    "category": "COMPACT_CAR"
                }]
            """,
            body,
            JSONCompareMode.LENIENT
        )
    }

    @Test
    fun findCarByRegistration_withUnknownRegistration_shouldReturn404() {
        val (status, body) = getCars(registration = "an-unknown-id")
        assertThat(status).isEqualTo(HttpStatus.OK)
        assertThat(body).isEqualTo("[]")
    }

    @Test
    fun deleteCar() {
        // Given
        val carId = repository.save(createCar("FIRST-CAR", "Lada", "Model1")).get().id

        // When
        val deletedCarResponse = deleteCar(carId = carId.toString())
        assertThat(deletedCarResponse.status).isEqualTo(HttpStatus.NO_CONTENT)
        val body = getCars().second
        assertThat(body).isEqualTo("[]")
    }

    private fun addCar(car: Car) = client.toBlocking()
        .exchange(HttpRequest.POST("/cars", car), String::class.java)

    private fun deleteCar(carId: String) = client.toBlocking()
        .exchange<Any, String>(HttpRequest.DELETE("/cars/$carId"), String::class.java)

    private fun updateCar(carId: String, car: Car) = client.toBlocking()
        .exchange(HttpRequest.PUT("/cars/$carId", car), String::class.java)

    private fun getCars(id: UUID? = null, registration: String? = null): Pair<HttpStatus, String> {
        val idPath = if (id != null) "/$id" else ""
        val registrationParam = if (registration != null) "?registration=$registration" else ""
        val response = client.toBlocking().exchange(HttpRequest.GET<Any>("/cars$idPath$registrationParam"), String::class.java)
        return Pair(response.status, response.body.get())
    }

    private fun createCar(registration: String, brand: String, model: String) = Car(
        registration = registration,
        brand = brand,
        model = model,
        category = CarCategory.COMPACT_CAR
    )
}
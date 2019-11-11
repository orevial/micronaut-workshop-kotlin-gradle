package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.agency.config.AppConfiguration
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import com.stacklabs.micronaut.workshop.agency.service.DataLoader
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class DataLoaderTest(private val carRepository: CarRepository, private val config: AppConfiguration) {

    private val expectedCars = mapOf(
        Pair(0, CarCategory.COUPE),
        Pair(1, CarCategory.CONVERTIBLE),
        Pair(2, CarCategory.MINIVAN),
        Pair(3, CarCategory.STATION_WAGON)
    )

    @BeforeEach
    fun setup() {
        carRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        carRepository.deleteAll()
    }

    @Test
    fun testDataLoader() {
        // Given
        val dataLoader = DataLoader(carRepository, config)

        // When
        dataLoader.onApplicationEvent(null)

        // Then
        val cars = carRepository.findAll()
        assertThat(cars).hasSameSizeAs(expectedCars.entries)

        expectedCars.entries
            .forEach {
                assertThat(cars[it.key].registration).isEqualTo("TEST-${it.key}")
                assertThat(cars[it.key].brand).isEqualTo("Brand${it.key}")
                assertThat(cars[it.key].model).isEqualTo("Model${it.key}")
                assertThat(cars[it.key].category).isEqualTo(it.value)
            }
    }
}
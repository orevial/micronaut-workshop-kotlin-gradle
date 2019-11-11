package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import com.stacklabs.micronaut.workshop.agency.service.DataLoader
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class DataLoaderTest(private val carRepository: CarRepository) {

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
        val dataLoader = DataLoader(carRepository)

        // When
        dataLoader.onApplicationEvent(null)

        // Then
        assertThat(carRepository.findAll()).hasSize(2)
    }
}
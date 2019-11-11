package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.agency.domain.Car
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import io.micronaut.test.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@MicronautTest
class CarRepositoryImplTest(private val repository: CarRepository) {

    private val tesla = createCar("AA-123-BB", "Tesla", "Model S", CarCategory.COUPE)
    private val clio = createCar("ZZ-666-NB", "Renault", "Clio", CarCategory.COMPACT_CAR)
    private val mini = createCar("ZZ-666-NB", "Mini", "Cooper", CarCategory.COMPACT_CAR)

    @Test
    fun testCrudOperations() {
        assertThat(repository.findAll()).hasSize(0)

        repository.save(tesla)
        repository.save(clio)
        assertThat(repository.findAll())
            .hasSize(2)
            .containsExactly(tesla, clio)

        assertThat(repository.findById(tesla.id!!)).hasValue(tesla)
        assertThat(repository.findByRegistration("ZZ-666-NB")).hasValue(clio)

        repository.update(clio.id!!, mini)
        val miniWithId = clio.copy(id = clio.id)
        assertThat(repository.findByRegistration("ZZ-666-NB")).hasValue(miniWithId)

        repository.deleteById(tesla.id!!)
        assertThat(repository.findAll()).hasSize(1)
    }

    private fun createCar(registration: String, brand: String, model: String, category: CarCategory) = Car(
        registration = registration,
        brand = brand,
        model = model,
        category = category
    )
}
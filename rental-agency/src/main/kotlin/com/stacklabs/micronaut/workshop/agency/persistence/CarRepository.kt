package com.stacklabs.micronaut.workshop.agency.persistence

import com.stacklabs.micronaut.workshop.agency.domain.Car
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

interface CarRepository {
    fun findAll(): List<Car>
    fun findById(@NotNull id: UUID): Optional<Car>
    fun findByRegistration(@NotNull registration: String?): Optional<Car>
    fun save(car: Car): Optional<Car>
    fun deleteById(@NotNull id: UUID)
    fun deleteAll(): Int
    fun update(@NotNull id: UUID, @NotBlank car: Car): Optional<Car>
}
package com.stacklabs.micronaut.workshop.agency.persistence

import com.stacklabs.micronaut.workshop.agency.domain.Car
import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession
import io.micronaut.spring.tx.annotation.Transactional
import java.util.*
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Singleton
open class CarRepositoryImpl(@CurrentSession @PersistenceContext private val entityManager: EntityManager) : CarRepository {

    @Transactional(readOnly = true)
    override fun findAll(): List<Car> =
        entityManager
            .createQuery("SELECT g FROM Car as g", Car::class.java)
            .resultList

    @Transactional(readOnly = true)
    override fun findById(@NotNull id: UUID): Optional<Car> =
        Optional.ofNullable(entityManager.find(Car::class.java, id))

    @Transactional(readOnly = true)
    override fun findByRegistration(@NotNull registration: String?): Optional<Car> =
        Optional.ofNullable(
            entityManager.createQuery("FROM Car where registration=:registration", Car::class.java)
                .setParameter("registration", registration)
                .resultList
                .firstOrNull()
        )

    @Transactional
    override fun save(car: Car): Optional<Car> {
        entityManager.persist(car)
        return findByRegistration(car.registration)
    }

    @Transactional
    override fun deleteById(@NotNull id: UUID) =
        findById(id).ifPresent { car -> entityManager.remove(car) }

    @Transactional
    override fun deleteAll() =
        entityManager
            .createQuery("DELETE FROM Car")
            .executeUpdate()

    @Transactional
    override fun update(@NotNull id: UUID, @NotBlank car: Car): Optional<Car> =
        findById(id).map {
            entityManager.createQuery(
                "UPDATE Car g SET brand = :brand, model = :model, category = :category, registration = :registration where id = :id")
                .setParameter("brand", car.brand)
                .setParameter("model", car.model)
                .setParameter("category", car.category)
                .setParameter("registration", car.registration)
                .setParameter("id", id)
                .executeUpdate()
        }.flatMap { i -> findById(id) }
}
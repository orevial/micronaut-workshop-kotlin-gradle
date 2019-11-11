package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.agency.aop.Logged
import com.stacklabs.micronaut.workshop.agency.domain.Car
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.hateoas.Link
import java.util.*
import javax.persistence.PersistenceException

@Controller("/cars")
open class CarsController(var repository: CarRepository) {

    @Logged
    @Get("/{?registration}")
    open fun findAll(@QueryValue("registration") registration: Optional<String>): List<Car> {
        return registration
            .map { reg -> repository.findByRegistration(reg).map { c -> listOf(c) }.orElseGet { emptyList() } }
            .orElseGet(repository::findAll)
    }

    @Logged
    @Get("/{id}")
    open fun findById(@PathVariable id: UUID): Optional<Car> =
        repository.findById(id)

    @Logged
    @Post("/")
    @Status(HttpStatus.CREATED)
    open fun add(@Body car: Car): Car =
        repository.save(car)
            .orElseThrow { RuntimeException("Unable to retrieve saved car...") }

    @Logged
    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    open fun delete(@PathVariable id: UUID): Unit =
        repository.deleteById(id)

    @Logged
    @Put("/{id}")
    open fun update(@PathVariable id: UUID, @Body car: Car): Optional<Car> =
        repository.update(id, car)

    @Error
    open fun jsonError(request: HttpRequest<String>, constraintViolationException: PersistenceException): HttpResponse<JsonError> {
        val error = JsonError("Duplicate record : " + constraintViolationException.message)
            .link(Link.SELF, Link.of(request.getUri()))

        return HttpResponse.status<JsonError>(HttpStatus.CONFLICT, "Given car already exists")
            .body(error)
    }
}
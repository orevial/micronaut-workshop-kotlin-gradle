package com.stacklabs.micronaut.workshop.agency

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
class CarsController(var repository: CarRepository) {

    @Get("/{?registration}")
    fun findAll(@QueryValue("registration") registration: Optional<String>): List<Car> {
        return registration
            .map { reg -> repository.findByRegistration(reg).map { c -> listOf(c) }.orElseGet { emptyList() } }
            .orElseGet(repository::findAll)
    }

    @Get("/{id}")
    fun findById(@PathVariable id: UUID): Optional<Car> =
        repository.findById(id)

    @Post("/")
    @Status(HttpStatus.CREATED)
    fun add(@Body car: Car): Car =
        repository.save(car)
            .orElseThrow { RuntimeException("Unable to retrieve saved car...") }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID): Unit =
        repository.deleteById(id)

    @Put("/{id}")
    fun update(@PathVariable id: UUID, @Body car: Car): Optional<Car> =
        repository.update(id, car)

    @Error
    fun jsonError(request: HttpRequest<String>, constraintViolationException: PersistenceException): HttpResponse<JsonError> {
        val error = JsonError("Duplicate record : " + constraintViolationException.message)
            .link(Link.SELF, Link.of(request.getUri()))

        return HttpResponse.status<JsonError>(HttpStatus.CONFLICT, "Given car already exists")
            .body(error)
    }
}
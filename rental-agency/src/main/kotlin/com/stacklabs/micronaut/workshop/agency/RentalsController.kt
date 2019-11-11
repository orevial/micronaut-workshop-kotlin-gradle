package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import java.util.*

@Controller("/cars/{id}/rentals")
class RentalsController(var repository: CarRepository, var calculator: PriceCalculatorClient) {

    @Get("/_price{?nbDays,kilometers,driverAge,insurance}")
    fun calculateRentalPrice(
        @PathVariable("id") carId: UUID,
        @QueryValue("nbDays") nbDays: Optional<Int>,
        @QueryValue("kilometers") kilometers: Optional<Int>,
        @QueryValue("driverAge") driverAge: Optional<Int>,
        @QueryValue("insurance") insurance: Optional<Boolean>)
        : HttpResponse<String> {

        return nbDays
            .flatMap { _nbDays -> kilometers
                .flatMap { _kilometers -> driverAge
                    .flatMap { _driverAge -> repository.findById(carId)
                        .map { _car -> RentalOptions(_car.category!!, _nbDays, _driverAge, _kilometers, insurance.orElseGet { false }) } } } }
            .flatMap { Optional.ofNullable(calculator.calculate(it).blockingGet()) }
            .map { HttpResponse.ok("This will cost you: $it€") }
            .orElseGet { HttpResponse.notFound() }
    }
}
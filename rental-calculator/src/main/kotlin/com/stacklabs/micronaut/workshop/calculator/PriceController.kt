package com.stacklabs.micronaut.workshop.calculator

import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory.*
import com.stacklabs.micronaut.workshop.api.v1.PriceCalculatorOperations
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions
import io.micronaut.http.annotation.Controller
import io.reactivex.Single

@Controller("/")
class PriceController : PriceCalculatorOperations {

    override fun calculate(options: RentalOptions): Single<Float> =
        Single.just(basePricePerDay(options.carCategory))
            .map { it * driverAgeCoef(options.driverAge) }
            .map { it * insuranceCoef(options.insurance) }
            .map { it + kilometersExtra(options.kilometers) }
            .map { it * options.nbDays }

    private fun driverAgeCoef(driverAge: Int) = if (driverAge > 30) 1F else 1.2F

    private fun insuranceCoef(insurance: Boolean) = if (insurance) 1.1F else 1F

    private fun kilometersExtra(kilometers: Int) = if (kilometers > 1000) 50 else 0

    private fun basePricePerDay(carCategory: CarCategory): Int =
        when (carCategory) {
            COMPACT_CAR -> 19
            SUV, STATION_WAGON -> 30
            MINIVAN -> 38
            COUPE, CONVERTIBLE -> 60
        }
}
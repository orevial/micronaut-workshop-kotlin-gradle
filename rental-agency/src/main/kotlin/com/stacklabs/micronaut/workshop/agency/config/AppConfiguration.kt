package com.stacklabs.micronaut.workshop.agency.config

import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("app")
class AppConfiguration {
    //var agencyId: String? = null
    var initialCars: List<ConfigCar>? = emptyList()

    @ConfigurationProperties("initial-cars")
    class ConfigCar {
        var brand: String? = null
        var model: String? = null
        var registration: String? = null
        var category: CarCategory? = null
    }
}
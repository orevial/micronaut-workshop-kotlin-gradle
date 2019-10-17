package com.stacklabs.micronaut.workshop.api.v1.model

import io.micronaut.core.annotation.Introspected

@Introspected
data class RentalOptions(val carCategory: CarCategory,
                         val nbDays: Int,
                         val driverAge: Int,
                         val kilometers: Int,
                         val insurance: Boolean)
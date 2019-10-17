package com.stacklabs.micronaut.workshop.api.v1.model

import io.micronaut.core.annotation.Introspected

@Introspected
enum class CarCategory {
    COMPACT_CAR, COUPE, CONVERTIBLE, SUV, MINIVAN, STATION_WAGON
}
package com.stacklabs.micronaut.workshop.api.v1

import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.reactivex.Single

interface PriceCalculatorOperations {
    @Post("/calculate")
    fun calculate(@Body options: RentalOptions): Single<Float>
}
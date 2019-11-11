package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.api.v1.PriceCalculatorOperations
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions
import io.micronaut.retry.annotation.Fallback
import io.reactivex.Single

@Fallback
class PriceCalculatorClientMock : PriceCalculatorOperations {
    override fun calculate(options: RentalOptions): Single<Float> = Single.just(10F)
}
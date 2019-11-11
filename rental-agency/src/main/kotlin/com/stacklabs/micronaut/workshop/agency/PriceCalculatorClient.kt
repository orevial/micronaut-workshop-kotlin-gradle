package com.stacklabs.micronaut.workshop.agency

import com.stacklabs.micronaut.workshop.api.v1.PriceCalculatorOperations
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Retryable

@Client("price-calculator")
@Retryable(attempts = "\${price-calculator.retry.attempts:3}", delay = "\${price-calculator.retry.delay:1s}")
interface PriceCalculatorClient : PriceCalculatorOperations {
}
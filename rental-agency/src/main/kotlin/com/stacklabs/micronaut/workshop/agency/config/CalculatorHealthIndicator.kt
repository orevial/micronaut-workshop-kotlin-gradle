package com.stacklabs.micronaut.workshop.agency.config

import com.stacklabs.micronaut.workshop.agency.PriceCalculatorClient
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import com.stacklabs.micronaut.workshop.api.v1.model.RentalOptions
import io.micronaut.health.HealthStatus
import io.micronaut.management.health.indicator.AbstractHealthIndicator
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import kotlin.system.measureTimeMillis

@Singleton
class CalculatorHealthIndicator(private var calculatorClient: PriceCalculatorClient) : AbstractHealthIndicator<Map<String, String>>() {
    companion object {
        val LOG = LoggerFactory.getLogger(CalculatorHealthIndicator::class.java)
    }

    override fun getName(): String {
        return "calculator-health"
    }

    override fun getHealthInformation(): Map<String, String> {
        val responseTime = measureTimeMillis { sampleRequest() }

        return mapOf(Pair("reponseTime", "$responseTime ms"))
    }

    fun sampleRequest() {
        calculatorClient.calculate(RentalOptions(CarCategory.COMPACT_CAR, 1, 50, 1000, false))
            .map { healthStatus = HealthStatus.UP }
            .doOnSuccess { LOG.info("Successful request made do rental-calculator server...") }
            .doOnError { healthStatus = HealthStatus.DOWN }
            .doOnError { LOG.error("Unable to reach rental-calculator server !") }
            .blockingGet()
    }
}
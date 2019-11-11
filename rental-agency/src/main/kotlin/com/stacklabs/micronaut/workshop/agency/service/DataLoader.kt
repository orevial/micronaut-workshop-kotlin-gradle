package com.stacklabs.micronaut.workshop.agency.service

import com.stacklabs.micronaut.workshop.agency.domain.Car
import com.stacklabs.micronaut.workshop.agency.persistence.CarRepository
import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.discovery.event.ServiceStartedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
@Requires(notEnv = [Environment.TEST])
class DataLoader(var repository: CarRepository) : ApplicationEventListener<ServiceStartedEvent> {

    companion object {
        val LOG: Logger = LoggerFactory.getLogger(DataLoader::class.java)
    }

    override fun onApplicationEvent(event: ServiceStartedEvent?) {
        listOf(
            Car(registration = "AA-982-BB", brand = "Tesla", model = "Model S", category = CarCategory.COUPE),
            Car(registration = "AA-123-BB", brand = "Ford", model = "Fiesta", category = CarCategory.COMPACT_CAR)
        )
            .onEach { LOG.info("Going to save car with registration ${it.registration}...") }
            .forEach { repository.save(it).ifPresentOrElse(
                { LOG.info("Successfuly saved car with registration ${it.registration}...") },
                { LOG.warn("Could not save car with registration ${it.registration}...") }
            )}
    }
}
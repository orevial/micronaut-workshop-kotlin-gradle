package com.stacklabs.micronaut.workshop.registry.config

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotBlank

@ConfigurationProperties("agencies")
class AgencyConfiguration {
    @NotBlank
    lateinit var databaseName: String

    @NotBlank
    lateinit var collectionName: String
}
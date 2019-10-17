package com.stacklabs.micronaut.workshop.api.v1.model

import io.micronaut.core.annotation.Introspected

@Introspected
data class Agency(var uuid: String = "some-uuid",
                  var name: String = "Some name")
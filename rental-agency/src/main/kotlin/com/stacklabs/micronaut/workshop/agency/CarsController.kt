package com.stacklabs.micronaut.workshop.agency

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/cars")
class CarsController {

    @Get("/")
    fun index() = "Hello Micronaut!"
}
package com.stacklabs.micronaut.workshop.agency

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.HttpStatus

@Controller("/cars")
class CarsController {

    @Get("/")
    fun index(): HttpStatus {
        return HttpStatus.OK
    }
}
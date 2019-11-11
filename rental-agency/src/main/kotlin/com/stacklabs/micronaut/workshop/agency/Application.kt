package com.stacklabs.micronaut.workshop.agency

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.stacklabs.micronaut.workshop.agency")
                .mainClass(Application.javaClass)
                .start()
    }
}
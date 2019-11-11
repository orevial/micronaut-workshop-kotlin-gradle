package com.stacklabs.micronaut.workshop.registry

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.stacklabs.micronaut.workshop.registry")
                .mainClass(Application.javaClass)
                .start()
    }
}
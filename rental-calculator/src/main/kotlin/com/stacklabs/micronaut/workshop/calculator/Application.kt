package com.stacklabs.micronaut.workshop.calculator

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("com.stacklabs.micronaut.workshop.calculator")
                .mainClass(Application.javaClass)
                .start()
    }
}
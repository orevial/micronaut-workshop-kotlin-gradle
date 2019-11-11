package com.stacklabs.micronaut.workshop.agency.aop

import io.micronaut.aop.Around
import io.micronaut.context.annotation.Type

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Around
@Type(LoggingAdvice::class)
annotation class Logged
package com.stacklabs.micronaut.workshop.api.v1

import com.stacklabs.micronaut.workshop.api.v1.model.Agency
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface AgencyOperations {

    @Get("/{id}")
    fun getById(@PathVariable("id") id: String): Maybe<Agency>

    @Get("/")
    fun list(): Flowable<Agency>

    @Post("/")
    fun save(@Body agency: Agency): Single<Agency>
}
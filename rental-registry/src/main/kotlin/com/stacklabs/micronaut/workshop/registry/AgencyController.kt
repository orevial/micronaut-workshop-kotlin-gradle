package com.stacklabs.micronaut.workshop.registry

import com.stacklabs.micronaut.workshop.api.v1.AgencyOperations
import com.stacklabs.micronaut.workshop.api.v1.model.Agency
import com.stacklabs.micronaut.workshop.registry.service.MongoService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

@Controller(value = "/agencies")
class AgencyController(private val mongoService: MongoService) : AgencyOperations {

    override fun getById(id: String): Maybe<Agency> = mongoService.getById(id)

    override fun list(): Flowable<Agency> = mongoService.list()

    override fun save(agency: Agency): Single<Agency> = mongoService.save(agency)

    @Get("/listWithDelay")
    fun listOneSecondEach(): Flowable<Agency> {
        return Flowable.zip(
            mongoService.list(),
            Flowable.interval(1, TimeUnit.SECONDS),
            BiFunction<Agency, Any, Agency> { item, _ -> item }
        )
    }

    // TODO should be at least password protected and reserved to admin accounts
    @Delete("/")
    fun deleteAll(): Single<String> = mongoService.deleteAll()
}
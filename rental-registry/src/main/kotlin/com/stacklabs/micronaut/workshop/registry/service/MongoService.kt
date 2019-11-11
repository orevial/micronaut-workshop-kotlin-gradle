package com.stacklabs.micronaut.workshop.registry.service

import com.mongodb.client.model.Filters.eq
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import com.mongodb.reactivestreams.client.Success
import com.stacklabs.micronaut.workshop.api.v1.AgencyOperations
import com.stacklabs.micronaut.workshop.api.v1.model.Agency
import com.stacklabs.micronaut.workshop.registry.config.AgencyConfiguration
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton


@Singleton
class MongoService(private val agencyConfiguration: AgencyConfiguration,
                   private val mongo: MongoClient) : AgencyOperations {

    companion object {
        val LOG = LoggerFactory.getLogger(MongoService::class.java)
    }

    override fun getById(id: String): Maybe<Agency> =
        Flowable.fromPublisher(
            getCollection()
                .find(eq("uuid", id))
                .limit(1)
        ).firstElement()

    override fun list(): Flowable<Agency> =
        Flowable
            .fromPublisher(getCollection().find())

    override fun save(agency: Agency): Single<Agency> {
        LOG.info("""Saving ${agency} agency in Mongo...""")
        return Single.just(agency)
            .map { a -> Agency(UUID.randomUUID().toString(), a.name) }
            .flatMap {
                a -> Single.fromPublisher<Success>(getCollection().insertOne(a))
                .map { """Successfuly saved ${a} agency to Mongo...""" }
                .map { a }
            }
    }

    fun deleteAll(): Single<String> {
        LOG.info("""Deleting entire ${agencyConfiguration.collectionName} collection from Mongo...""")
        return Single.fromPublisher<Success>(getCollection().drop())
                .map { "Successfuly deleted entire $ from Mongo..." }
    }

    private fun getCollection(): MongoCollection<Agency> =
        mongo
            .getDatabase(agencyConfiguration.databaseName)
            .getCollection(agencyConfiguration.collectionName, Agency::class.java)
}
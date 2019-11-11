package com.stacklabs.micronaut.workshop.agency.domain

import com.stacklabs.micronaut.workshop.api.v1.model.CarCategory
import io.micronaut.http.hateoas.Link
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "cars")
data class Car(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = UUID.fromString("00000000-0000-0000-0000-000000000000"),

    @NotNull
    @Column(name = "registration", nullable = false, unique = true)
    val registration: String? = null,

    @NotNull
    @Column(name = "brand", nullable = false)
    val brand: String? = null,

    @NotNull
    @Column(name = "model", nullable = false)
    val model: String? = null,

    @NotNull
    @Column(name = "category", nullable = false)
    val category: CarCategory? = null,

    @Transient
    val links: List<Link>? = null
)
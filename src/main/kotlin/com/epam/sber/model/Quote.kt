package com.epam.sber.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator

@Entity
data class Quote(
    @Id
    @GeneratedValue(generator = "quote_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(
        name = "quote_id_seq",
        sequenceName = "quote_id_seq",
        allocationSize = 50
    )
    val id: Long? = null,
    @Column(nullable = false, unique = false)
    val isin: String,
    @Column(nullable = true, unique = false)
    val bid: Float? = null,
    @Column(nullable = true, unique = false)
    val ask: Float? = null
)


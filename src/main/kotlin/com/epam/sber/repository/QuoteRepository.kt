package com.epam.sber.repository

import com.epam.sber.model.Quote
import org.springframework.data.repository.CrudRepository

interface QuoteRepository : CrudRepository<Quote, Long>
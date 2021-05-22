package com.epam.sber.service.impl

import com.epam.sber.dto.ElvlDto
import com.epam.sber.dto.QuoteDto
import com.epam.sber.repository.QuoteRepository
import com.epam.sber.service.QuoteQueue
import com.epam.sber.service.QuoteService
import org.springframework.stereotype.Service

@Service
class QuoteServiceImpl(val quoteQueue: QuoteQueue, val quoteRepository: QuoteRepository) : QuoteService {

    override fun produce(quotes: List<QuoteDto>) {
        quoteQueue.produce(quotes)
    }

    override fun elvlByIsin(isin: String): Float? {
        return quoteQueue.elvlByIsin(isin)
    }

    override fun elvls(): List<ElvlDto> {
        return quoteQueue.elvls()
    }
}
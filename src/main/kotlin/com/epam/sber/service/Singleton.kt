package com.epam.sber.service

import com.epam.sber.dto.QuoteDto
import java.util.ArrayList
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap

object Singleton {
    private val queue: BlockingQueue<QuoteDto>
    private val elvls: MutableMap<String,Float>
    init {
        queue = ArrayBlockingQueue(1000, true)
        elvls = ConcurrentHashMap()
    }

    @Synchronized fun putQuote(quoteDto: QuoteDto) {
        queue.put(quoteDto)
    }

    @Synchronized fun putElvls(quoteDto: QuoteDto) {
        elvls.put(quoteDto.isin, quoteDto.getElvl())
    }

    @Synchronized fun getQuotes(): MutableCollection<QuoteDto> {
        val quotes: MutableCollection<QuoteDto> = ArrayList()
        queue.drainTo(quotes)
        return quotes
    }

    @Synchronized fun getElvls(): MutableMap<String,Float> {
        return elvls
    }
}
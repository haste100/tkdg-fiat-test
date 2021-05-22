package com.epam.sber.service

import com.epam.sber.repository.QuoteRepository
import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Service

@Service
class QuoteService(val quoteRepository: QuoteRepository) {

    private val logger = logger()

    internal inner class Consumer : Runnable {
        override fun run() {
            try {
                while (true) {
                    val quotes = Singleton.getQuotes()
                    if (!quotes.isEmpty()) {
                        val list = quotes.map { it.toQuote() }
                        quoteRepository.saveAll(list)
                        logger.info("Saved " + list.size + " quotes")
                    }
                }
            } catch (e: InterruptedException) {
                logger.error(""+e.message)
            }
        }
    }

    init {
        Thread(Consumer()).start()
    }
}
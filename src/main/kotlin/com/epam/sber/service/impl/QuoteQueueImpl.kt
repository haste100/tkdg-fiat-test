package com.epam.sber.service.impl

import com.epam.sber.dto.ElvlDto
import com.epam.sber.dto.QuoteDto
import com.epam.sber.repository.QuoteRepository
import com.epam.sber.service.QuoteQueue
import org.apache.logging.log4j.kotlin.logger
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ConcurrentHashMap

@Service
class QuoteQueueImpl(val quoteRepository: QuoteRepository): QuoteQueue {

    private val logger = logger()

    private val queue: BlockingQueue<QuoteDto>
    private val elvls: MutableMap<String,Float>

    override fun produce(quotes: List<QuoteDto>) {
        quotes.filter(QuoteDto::isValid)
            .forEach{
                queue.put(it)
                elvls.put(it.isin, it.getElvl())
            }
    }

    override fun elvlByIsin(isin:String): Float? {
        return elvls.get(isin)
    }

    override fun elvls(): List<ElvlDto> {
        return elvls.map { (k,v) -> ElvlDto(k, v) }
    }

    internal inner class Consumer : Runnable {
        override fun run() {
            try {
                while (true) {
                    val quotes: MutableCollection<QuoteDto> = ArrayList()
                    queue.drainTo(quotes)
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
        queue = ArrayBlockingQueue(1000, true)
        elvls = ConcurrentHashMap()
        Thread(Consumer()).start()
    }
}
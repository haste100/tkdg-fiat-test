package com.epam.sber.service

import com.epam.sber.dto.ElvlDto
import com.epam.sber.dto.QuoteDto

interface QuoteQueue {
    fun produce(quotes: List<QuoteDto>)
    fun elvlByIsin(isin:String): Float?
    fun elvls(): List<ElvlDto>
}
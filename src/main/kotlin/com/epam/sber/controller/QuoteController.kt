package com.epam.sber.controller

import com.epam.sber.constant.ApiUrl
import com.epam.sber.dto.BaseResponseDto
import com.epam.sber.dto.EnergyLevelDto
import com.epam.sber.dto.QuoteDto
import com.epam.sber.service.Singleton
import com.epam.sber.service.Singleton.putElvls
import com.epam.sber.service.Singleton.putQuote
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Api(value=ApiUrl.QUOTES, description = "Сервис получает котировки")
@RestController
class QuoteController() {

    @PostMapping(value = [ApiUrl.QUOTES], consumes= [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun addQuotes(@RequestBody quotes: List<QuoteDto>): ResponseEntity<BaseResponseDto> {
        quotes.filter(QuoteDto::isValid)
            .forEach {
                putQuote(it)
                putElvls(it)
            }
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto())
    }

    @GetMapping(value = [ApiUrl.ELVL_BY_ISIN])
    fun elvlByIsin(@RequestParam("isin")isin: String): ResponseEntity<BaseResponseDto> {
        val elvl = Singleton.getElvls()[isin]
        val body = BaseResponseDto(listOf(elvl as Any))
        return ResponseEntity.status(HttpStatus.OK).body(body)
    }

    @GetMapping(value = [ApiUrl.ELVLS])
    fun elvls(): ResponseEntity<BaseResponseDto> {
        val data = Singleton.getElvls().map { (k,v) -> EnergyLevelDto(k, v) }
        val body = BaseResponseDto(data)
        return ResponseEntity.status(HttpStatus.OK).body(body)
    }
}
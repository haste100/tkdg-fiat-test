package com.epam.sber.dto

data class BaseResponseDto(
    val data: List<Any> = emptyList(),
    val errors: List<Any> = emptyList(),
    val meta: List<Any> = emptyList()
)

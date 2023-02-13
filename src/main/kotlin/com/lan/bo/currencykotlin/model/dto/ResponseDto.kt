package com.lan.bo.currencykotlin.model.dto

class ResponseDto<A>(
    val data:A?,
    val message: String?,
    val success:Boolean
)
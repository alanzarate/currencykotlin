package com.lan.bo.currencykotlin.api

import com.lan.bo.currencykotlin.bl.CurrencyBla
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lan.bo.currencykotlin.model.dto.ResponseDto
import okhttp3.OkHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/currency")
class CurrencyApi (
    private val currencyBl: CurrencyBla
)
{
    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
    private val logger: Logger = LoggerFactory.getLogger(CurrencyApi::class.java)


    @GetMapping("/convert")
    fun controllerMethod(@RequestParam customQuery:Map<String, String>):ResponseDto<Any>{
        val name = "Sam"
        logger.info("Hi, $name  info log" )
        logger.info("Dummy info message")
        logger.warn("Dummy warning message.")
        logger.error("Dummy error message.")

        val res:ResponseDto<Any> = currencyBl.getData(customQuery["from"], customQuery["to"], customQuery["amount"])
        println(res.data)


        return res
    }
}
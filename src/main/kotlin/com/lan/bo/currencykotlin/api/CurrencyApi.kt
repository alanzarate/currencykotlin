package com.lan.bo.currencykotlin.api

import com.lan.bo.currencykotlin.bl.CurrencyBla

import com.lan.bo.currencykotlin.model.dto.ResponseDto
import okhttp3.OkHttpClient

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




    @GetMapping("/convert")
    fun controllerMethod(@RequestParam customQuery:Map<String, String>):ResponseDto<Any>{
        val name = "Sam"


        val res:ResponseDto<Any> = currencyBl.getData(customQuery["from"], customQuery["to"], customQuery["amount"])
        println(res.data)


        return res
    }

    @GetMapping("/all")
    fun allMethod(@RequestParam customQuery: Map<String, String>): ResponseDto<Any> {
        val limit = customQuery["limit"]
        val currentPage = customQuery["currentPage"]
        val sortBy = customQuery["sortBy"]
        val order = customQuery["order"]
        print("limite: $limit , currentPage: $currentPage , sortBy: $sortBy")

        return currencyBl.getAllData(limit, currentPage, sortBy, order)
    }

}
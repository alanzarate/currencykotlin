package com.lan.bo.currencykotlin.service


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lan.bo.currencykotlin.model.ErrorModel
import com.lan.bo.currencykotlin.model.InformationModel
import com.lan.bo.currencykotlin.model.exception.ParameterException
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CurrencyService {
    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()


    @Value("\${api.url}")
    lateinit var apiUrl: String

    @Value("\${api.key}")
    lateinit var apiKey: String

    fun responseByCurrencyApi(from:String?, to:String?, amount: BigDecimal?): InformationModel {
        print(apiKey)

        val request = okhttp3.Request.Builder()
            .url("$apiUrl?from=$from&to=$to&amount=$amount")
            .addHeader("apikey", apiKey)
            .build()
        val responseBody = client.newCall(request).execute().use {
                response ->
            if(response.code!= 200){
                val err:ErrorModel = mapper.readValue(response.body!!.string(), ErrorModel::class.java)
                if(response.code == 400) throw ParameterException(err.error["code"]!!)

            }

            return@use response.body!!.string()
        }
        return mapper.readValue(responseBody, InformationModel::class.java)

    }
}
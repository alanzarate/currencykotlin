package com.lan.bo.currencykotlin.service


import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lan.bo.currencykotlin.model.ErrorModel
import com.lan.bo.currencykotlin.model.InformationModel
import com.lan.bo.currencykotlin.model.exception.ParameterException
import okhttp3.OkHttpClient
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CurrencyService {
    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
    fun responseByCurrencyApi(from:String?, to:String?, amount: BigDecimal?): InformationModel {

        println("request: https://api.apilayer.com/exchangerates_data/convert?from=$from&to=$to&amount=$amount")
        val request = okhttp3.Request.Builder()
            .url("https://api.apilayer.com/exchangerates_data/convert?from=$from&to=$to&amount=$amount")
            .addHeader("apikey", "jqPB058UIYBeQoPFJNMfd73RtwJEjFcK")
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
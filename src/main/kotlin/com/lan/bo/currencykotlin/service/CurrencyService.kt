package com.lan.bo.currencykotlin.service



import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lan.bo.currencykotlin.model.ErrorModel
import com.lan.bo.currencykotlin.model.InformationModel
import com.lan.bo.currencykotlin.model.SymbolDto
import com.lan.bo.currencykotlin.model.TimeSeriesMode
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

    fun getTimeSeries(startDate: String?, endDate: String?, symbols: Array<String>, base: String): TimeSeriesMode{
        val request = okhttp3.Request.Builder()
            //.url("https://run.mocky.io/v3/d3766d5f-7335-4d8d-a9f8-760b2468d6bc")
            .url("https://api.apilayer.com/exchangerates_data/timeseries?start_date="+startDate+"&end_date="+endDate+"&symbols="+symbols[0]+"&base="+base)
            .addHeader("apikey", apiKey)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw ParameterException("Unexpected code $response")

            return mapper.readValue(response.body!!.string(), TimeSeriesMode::class.java)
        }

    }

    fun getSymbols(): SymbolDto{
        val request = okhttp3.Request.Builder()
            .url("https://api.apilayer.com/exchangerates_data/symbols")
            .addHeader("apikey", apiKey)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw ParameterException("Unexpected code $response")

            return mapper.readValue(response.body!!.string(), SymbolDto::class.java)
        }

    }
}
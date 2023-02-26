package com.lan.bo.currencykotlin.bl

import com.lan.bo.currencykotlin.model.Currency
import com.lan.bo.currencykotlin.model.InformationModel
import com.lan.bo.currencykotlin.model.dto.ResponseDto
import com.lan.bo.currencykotlin.model.exception.ParameterException
import com.lan.bo.currencykotlin.model.repo.CurrencyRepository
import com.lan.bo.currencykotlin.service.CurrencyService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*


@Service
class CurrencyBla(
    private val currencyService: CurrencyService,
    private val currencyRepository: CurrencyRepository
)
{
    companion object{
        val LOGGER: Logger = LoggerFactory.getLogger(CurrencyBla::class.java)
    }

    @Value("\${api.url}")
    lateinit var apiUrl: String

    @Value("\${api.key}")
    lateinit var apiKey: String

    fun getData(from:String?, to:String?, amount:String?):ResponseDto<Any>{
        var mess:String = ""
        try{

//            if(from.isNullOrBlank()) throw ParameterException("invalid_from_currency")
//            if(to.isNullOrBlank()) throw ParameterException("invalid_to_currency")

            val response:InformationModel = currencyService.responseByCurrencyApi(from, to, amount?.toBigDecimal())

            structData(response)

            println(response)
            return ResponseDto(response, null, true )

        }catch (ex:NumberFormatException){
            mess = "Verificar el monto para la conversion (1)"

        }catch (ex:ParameterException){
            when (ex.message) {
                "invalid_conversion_amount" -> mess = "Verificar el monto para la conversion"
                "invalid_to_currency" -> mess = "Parametro (to) incorrecto"
                "invalid_from_currency" -> mess = "Parametro (from) incorrecto"
            }
        }catch (ex:SocketTimeoutException){
            mess = "Timeout"
        }catch (ex:IOException){
            mess = ex.message.toString()
        }

        return ResponseDto(null, mess, false )


    }


    private fun structData(response: InformationModel) {
        val currency =  Currency()
        currency.currencyFrom = response.query["from"].toString()
        currency.currencyTo = response.query["to"].toString()
        currency.amount = response.query["amount"].toString().toBigDecimal()
        //currency.date = SimpleDateFormat("yyyy-MM-dd").parse(response.date)
        currency.date = Date()
        currency.result = response.result
        currencyRepository.save(currency)


    }

}
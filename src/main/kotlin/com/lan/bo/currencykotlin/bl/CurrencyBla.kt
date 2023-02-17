package com.lan.bo.currencykotlin.bl

import com.lan.bo.currencykotlin.model.InformationModel
import com.lan.bo.currencykotlin.model.dto.ResponseDto
import com.lan.bo.currencykotlin.model.exception.ParameterException
import com.lan.bo.currencykotlin.service.CurrencyService
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.SocketTimeoutException


@Service
class CurrencyBla(
    private val currencyService: CurrencyService
)
{
    fun getData(from:String?, to:String?, amount:String?):ResponseDto<Any>{
        var mess:String = ""
        try{

//            if(from.isNullOrBlank()) throw ParameterException("invalid_from_currency")
//            if(to.isNullOrBlank()) throw ParameterException("invalid_to_currency")

            val response:InformationModel = currencyService.responseByCurrencyApi(from, to, amount?.toBigDecimal())
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


    // i dont give a shit about this 
    // this is a grocery for future
    
}
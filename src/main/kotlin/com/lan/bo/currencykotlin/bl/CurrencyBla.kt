package com.lan.bo.currencykotlin.bl

import com.lan.bo.currencykotlin.model.Currency
import com.lan.bo.currencykotlin.model.InformationModel
import com.lan.bo.currencykotlin.model.dto.ResponseDto
import com.lan.bo.currencykotlin.model.exception.ParameterException
import com.lan.bo.currencykotlin.model.repo.CurrencyRepository
import com.lan.bo.currencykotlin.service.CurrencyService

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.ceil


@Service
class CurrencyBla(
    private val currencyService: CurrencyService,
    private val currencyRepository: CurrencyRepository
)
{


    @Value("\${api.url}")
    lateinit var apiUrl: String

    @Value("\${api.key}")
    lateinit var apiKey: String


    fun getAllData(limit: String?, currentPage: String?, sortBy: String?, order: String?): ResponseDto<Any> {



        var message = ""

        try {

            var lim = Integer.parseInt(limit)
            var curPage = Integer.parseInt(currentPage)

            var perPage = lim
            var offset = ( curPage - 1) * perPage
            var length = currencyRepository.count()


            var pages = (ceil(length / perPage.toDouble())).toInt()
            var pagin: MutableIterable<Currency>
            if (!sortBy.isNullOrBlank()){
                // sorting elements

//                var field= "betean"
//                var list_sorted = currencyRepository.findAll(Sort.by(Sort.Direction.ASC, field))
                // pagination and sorting
                if (order.isNullOrBlank() ) throw ParameterException("invalid_order");

                pagin = if(order == "asc"){
                    currencyRepository.findAll(PageRequest.of((curPage-1), lim).withSort(Sort.by(Sort.Direction.ASC, sortBy)))

                }else{
                    currencyRepository.findAll(PageRequest.of((curPage-1), lim).withSort(Sort.by(Sort.Direction.DESC, sortBy)))
                }


            }else{
                // pagination
                pagin = currencyRepository.findAll(PageRequest.of((curPage-1), lim))

            }


            // all generic
//            var list = currencyRepository.findAll()

            var ls = ArrayList<HashMap<String, Any>>()
            for (currency in pagin) {
                var mapArq = HashMap<String, Any>()
                mapArq["id"] = currency.id
                mapArq["from"] = currency.currencyFrom
                mapArq["to"] = currency.currencyTo
                mapArq["amount"] = currency.amount
                mapArq["result"] = currency.result
                mapArq["date"] = currency.date.toString()
                ls.add(mapArq)
            }

            var newMap = HashMap<String, Any>()
            newMap["limit"] = lim
            newMap["currentPage"] = curPage
            newMap["totalPages"] = pages
            newMap["values"] = ls
            newMap["totalValues"] = length
            return ResponseDto(newMap, null, true)
        }catch (ex: Exception){
            print(ex)
            message = ex.message.toString()
        }
        return ResponseDto(null, message, false)
    }

    fun getData(from:String?, to:String?, amount:String?):ResponseDto<Any>{
        var mess:String = ""
        try{

//            if(from.isNullOrBlank()) throw ParameterException("invalid_from_currency")
//            if(to.isNullOrBlank()) throw ParameterException("invalid_to_currency")

            val response:InformationModel = currencyService.responseByCurrencyApi(from, to, amount?.toBigDecimal())

            structData(response)

            //var currency: Currency = constructObject(response)
            //return ResponseDto(response, null, true )

            val currency: Map<String, Any> = constructObject(response)
            return ResponseDto(currency, null, true )

        }catch (ex:NumberFormatException){
            mess = "Verificar el monto para la conversion (1)"

        }catch (ex:ParameterException){
            when (ex.message) {
                "invalid_conversion_amount" -> mess = "Verificar el monto para la conversion"
                "invalid_to_currency" -> mess = "Parametro (to) incorrecto"
                "invalid_from_currency" -> mess = "Parametro (from) incorrecto"
                "invalid_order" -> mess = "Parametro (order) incorrecto"
            }
        }catch (ex:SocketTimeoutException){
            mess = "Timeout"
        }catch (ex:IOException){
            mess = ex.message.toString()
        }

        return ResponseDto(null, mess, false )


    }

    private fun constructObject(response: InformationModel):Map<String, Any> {
        var currency: HashMap<String, Any> =  HashMap<String,Any>()
        currency.put("from", response.query["from"].toString())
        currency.put("to", response.query["to"].toString())        
        currency.put("amount" , response.query["amount"].toString().toBigDecimal())
        currency.put("date" , SimpleDateFormat("yyyy-MM-dd").parse(response.date))
        //currency.put("date" , Date())
        currency.put("result" , response.result)
        
        return currency

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
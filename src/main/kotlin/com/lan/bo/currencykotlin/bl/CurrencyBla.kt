package com.lan.bo.currencykotlin.bl

import com.lan.bo.currencykotlin.api.TimeSeries
import com.lan.bo.currencykotlin.model.*
import com.lan.bo.currencykotlin.model.Currency
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

    fun getTimeSeries(base:String, symbol:String, start:String?, end:String?): ResponseDto<TimeSeries> {
        val response = currencyService.getTimeSeries(start, end, arrayOf(symbol), base)
        var adapter: AdapterTimeSeries = AdapterTimeSeries(response)
        adapter.setBase(response.base)
        //adapter.setData()
        return ResponseDto(adapter, null, true)
    }

    fun getSymbols(): ResponseDto<Map<String, String>>{
        //val value = currencyService.getSymbols()
        var mapa: Map<String, String> = mapOf(
            "BOB" to "Bolivian Boliviano",
            "USD" to "United States Dollar",
            "AED" to "United Arab Emirates Dirham",
        "AFN" to "Afghan Afghani",
        "ALL" to "Albanian Lek",
        "AMD" to "Armenian Dram",
        "ANG" to "Netherlands Antillean Guilder",
        "AOA" to "Angolan Kwanza",
        "ARS" to "Argentine Peso",
        "AUD" to "Australian Dollar",
        "AWG" to "Aruban Florin",
        "AZN" to "Azerbaijani Manat",
        "BAM" to "Bosnia-Herzegovina Convertible Mark",
        "BBD" to "Barbadian Dollar",
        "BDT" to "Bangladeshi Taka",
        "BGN" to "Bulgarian Lev",
        "BHD" to "Bahraini Dinar",
        "BIF" to "Burundian Franc",
        "BMD" to "Bermudan Dollar",
        "BND" to "Brunei Dollar",
        "BRL" to "Brazilian Real",
        "BSD" to "Bahamian Dollar",
        "BTC" to "Bitcoin",
        "BTN" to "Bhutanese Ngultrum",
        "BWP" to "Botswanan Pula",
        "BYN" to "New Belarusian Ruble",
        "BYR" to "Belarusian Ruble",
        "BZD" to "Belize Dollar",
        "CAD" to "Canadian Dollar",
        "CDF" to "Congolese Franc",
        "CHF" to "Swiss Franc",
        "CLF" to "Chilean Unit of Account (UF)",
        "CLP" to "Chilean Peso",
        "CNY" to "Chinese Yuan",
        "COP" to "Colombian Peso",
        "CRC" to "Costa Rican Colón",
        "CUC" to "Cuban Convertible Peso",
        "CUP" to "Cuban Peso",
        "CVE" to "Cape Verdean Escudo",
        "CZK" to "Czech Republic Koruna",
        "DJF" to "Djiboutian Franc",
        "DKK" to "Danish Krone",
        "DOP" to "Dominican Peso",
        "DZD" to "Algerian Dinar",
        "EGP" to "Egyptian Pound",
        "ERN" to "Eritrean Nakfa",
        "ETB" to "Ethiopian Birr",
        "EUR" to "Euro",
        "FJD" to "Fijian Dollar",
        "FKP" to "Falkland Islands Pound",
        "GBP" to "British Pound Sterling",
        "GEL" to "Georgian Lari",
        "GGP" to "Guernsey Pound",
        "GHS" to "Ghanaian Cedi",
        "GIP" to "Gibraltar Pound",
        "GMD" to "Gambian Dalasi",
        "GNF" to "Guinean Franc",
        "GTQ" to "Guatemalan Quetzal",
        "GYD" to "Guyanaese Dollar",
        "HKD" to "Hong Kong Dollar",
        "HNL" to "Honduran Lempira",
        "HRK" to "Croatian Kuna",
        "HTG" to "Haitian Gourde",
        "HUF" to "Hungarian Forint",
        "IDR" to "Indonesian Rupiah",
        "ILS" to "Israeli New Sheqel",
        "IMP" to "Manx pound",
        "INR" to "Indian Rupee",
        "IQD" to "Iraqi Dinar",
        "IRR" to "Iranian Rial",
        "ISK" to "Icelandic Króna",
        "JEP" to "Jersey Pound",
        "JMD" to "Jamaican Dollar",
        "JOD" to "Jordanian Dinar",
        "JPY" to "Japanese Yen",
        "KES" to "Kenyan Shilling",
        "KGS" to "Kyrgystani Som",
        "KHR" to "Cambodian Riel",
        "KMF" to "Comorian Franc",
        "KPW" to "North Korean Won",
        "KRW" to "South Korean Won",
        "KWD" to "Kuwaiti Dinar",
        "KYD" to "Cayman Islands Dollar",
        "KZT" to "Kazakhstani Tenge",
        "LAK" to "Laotian Kip",
        "LBP" to "Lebanese Pound",
        "LKR" to "Sri Lankan Rupee",
        "LRD" to "Liberian Dollar",
        "LSL" to "Lesotho Loti",
        "LTL" to "Lithuanian Litas",
        "LVL" to "Latvian Lats",
        "LYD" to "Libyan Dinar",
        "MAD" to "Moroccan Dirham",
        "MDL" to "Moldovan Leu",
        "MGA" to "Malagasy Ariary",
        "MKD" to "Macedonian Denar",
        "MMK" to "Myanma Kyat",
        "MNT" to "Mongolian Tugrik",
        "MOP" to "Macanese Pataca",
        "MRO" to "Mauritanian Ouguiya",
        "MUR" to "Mauritian Rupee",
        "MVR" to "Maldivian Rufiyaa",
        "MWK" to "Malawian Kwacha",
        "MXN" to "Mexican Peso",
        "MYR" to "Malaysian Ringgit",
        "MZN" to "Mozambican Metical",
        "NAD" to "Namibian Dollar",
        "NGN" to "Nigerian Naira",
        "NIO" to "Nicaraguan Córdoba",
        "NOK" to "Norwegian Krone",
        "NPR" to "Nepalese Rupee",
        "NZD" to "New Zealand Dollar",
        "OMR" to "Omani Rial",
        "PAB" to "Panamanian Balboa",
        "PEN" to "Peruvian Nuevo Sol",
        "PGK" to "Papua New Guinean Kina",
        "PHP" to "Philippine Peso",
        "PKR" to "Pakistani Rupee",
        "PLN" to "Polish Zloty",
        "PYG" to "Paraguayan Guarani",
        "QAR" to "Qatari Rial",
        "RON" to "Romanian Leu",
        "RSD" to "Serbian Dinar",
        "RUB" to "Russian Ruble",
        "RWF" to "Rwandan Franc",
        "SAR" to "Saudi Riyal",
        "SBD" to "Solomon Islands Dollar",
        "SCR" to "Seychellois Rupee",
        "SDG" to "Sudanese Pound",
        "SEK" to "Swedish Krona",
        "SGD" to "Singapore Dollar",
        "SHP" to "Saint Helena Pound",
        "SLE" to "Sierra Leonean Leone",
        "SLL" to "Sierra Leonean Leone",
        "SOS" to "Somali Shilling",
        "SRD" to "Surinamese Dollar",
        "STD" to "São Tomé and Príncipe Dobra",
        "SVC" to "Salvadoran Colón",
        "SYP" to "Syrian Pound",
        "SZL" to "Swazi Lilangeni",
        "THB" to "Thai Baht",
        "TJS" to "Tajikistani Somoni",
        "TMT" to "Turkmenistani Manat",
        "TND" to "Tunisian Dinar",
        "TOP" to "Tongan Paʻanga",
        "TRY" to "Turkish Lira",
        "TTD" to "Trinidad and Tobago Dollar",
        "TWD" to "New Taiwan Dollar",
        "TZS" to "Tanzanian Shilling",
        "UAH" to "Ukrainian Hryvnia",
        "UGX" to "Ugandan Shilling",
        "UYU" to "Uruguayan Peso",
        "UZS" to "Uzbekistan Som",
        "VEF" to "Venezuelan Bolívar Fuerte",
        "VES" to "Sovereign Bolivar",
        "VND" to "Vietnamese Dong",
        "VUV" to "Vanuatu Vatu",
        "WST" to "Samoan Tala",
        "XAF" to "CFA Franc BEAC",
        "XAG" to "Silver (troy ounce)",
        "XAU" to "Gold (troy ounce)",
        "XCD" to "East Caribbean Dollar",
        "XDR" to "Special Drawing Rights",
        "XOF" to "CFA Franc BCEAO",
        "XPF" to "CFP Franc",
        "YER" to "Yemeni Rial",
        "ZAR" to "South African Rand",
        "ZMK" to "Zambian Kwacha (pre-2013)",
        "ZMW" to "Zambian Kwacha",
        "ZWL" to "Zimbabwean Dollar");
        return ResponseDto(mapa, null, true)
    }




}
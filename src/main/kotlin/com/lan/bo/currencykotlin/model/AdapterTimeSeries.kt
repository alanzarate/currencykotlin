package com.lan.bo.currencykotlin.model

import com.lan.bo.currencykotlin.api.TimeSeries
import javax.print.DocFlavor.STRING

class AdapterTimeSeries
    (private var timeSeriesMode: TimeSeriesMode) : TimeSeries(){

        override fun getData(): HashMap<String, HashMap<String, Double>> {
            var currencies: Array<String> = arrayOf()
            var dates: Array<String> = arrayOf()

            val first = timeSeriesMode.rates.keys.first()

            val mapFirst = timeSeriesMode.rates[first]
            mapFirst?.forEach { entry ->
                currencies += entry.key
            }

            val datess = timeSeriesMode.rates.keys
            val map0: HashMap<String, HashMap<String, Double>> = HashMap()


            currencies.forEach { e ->

                val map1: LinkedHashMap<String, Double> = LinkedHashMap()
                datess.forEach { e2 ->
                    map1[e2] = timeSeriesMode.rates[e2]!![e]!!
                }
                map0[e] = map1
            }
            return map0
        }
}
package com.lan.bo.currencykotlin.api

open class TimeSeries {
    private lateinit var base:String
    private lateinit var rates: HashMap<String,HashMap<String, Double>>

    fun addToRates(title:String, map:HashMap<String, Double>){
        rates[title] = map
    }

    open fun getData(): HashMap<String, HashMap<String, Double>> {
        return rates;
    }

    open fun setData(){
        this.rates = getData()
    }
    fun setBase(base:String){
        this.base = base
    }


    fun getBase(): String{
        return base
    }
}
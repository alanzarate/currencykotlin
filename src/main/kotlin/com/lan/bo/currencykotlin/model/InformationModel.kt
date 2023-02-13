package com.lan.bo.currencykotlin.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
data class InformationModel (
    var success: Boolean,
    var date:String,
    var query: Map<String, Any>,
    var result: BigDecimal,
    var fool: String?
){

    fun fromJson(){

    }
}
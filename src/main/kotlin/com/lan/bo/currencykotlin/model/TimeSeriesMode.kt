package com.lan.bo.currencykotlin.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TimeSeriesMode(
    @JsonProperty("success")
    var success: Boolean,
    @JsonProperty("timeseries")
    var timeseries: Boolean,
    @JsonProperty("start_date")
    var start_date: String,
    @JsonProperty("end_date")
    var end_date: String,
    @JsonProperty("base")
    var base:String,
    @JsonProperty("rates")
    var rates: Map<String, Map<String, Double>>
) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Rate(
    var rat: Map<String, Double>
){

}
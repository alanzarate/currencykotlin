package com.lan.bo.currencykotlin.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class SymbolDto(
    @JsonProperty("symbols")
    var symbols: HashMap<String, String>
) {
}
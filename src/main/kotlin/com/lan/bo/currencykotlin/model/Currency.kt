package com.lan.bo.currencykotlin.model


import java.math.BigDecimal
import java.util.Date
import javax.persistence.*


@Entity
@Table(name = "currency_table")
class Currency (
    var currencyFrom : String,
    var currencyTo: String,
    var amount: BigDecimal,
    var result: BigDecimal,
    var date: Date,
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0
){
    constructor() : this("", "",BigDecimal.ZERO, BigDecimal.ZERO, Date()  ){

    }
}
package com.lan.bo.currencykotlin.model.repo

import com.lan.bo.currencykotlin.model.Currency
import org.springframework.data.repository.CrudRepository

interface CurrencyRepository: CrudRepository<Currency, Long> {
}
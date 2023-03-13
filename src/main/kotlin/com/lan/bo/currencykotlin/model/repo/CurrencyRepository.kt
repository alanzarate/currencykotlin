package com.lan.bo.currencykotlin.model.repo

import com.lan.bo.currencykotlin.model.Currency
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.CrudRepository

interface CurrencyRepository: CrudRepository<Currency, Long> {
    fun findAll(by: Sort): MutableIterable<Currency>

    fun findAll(page: Pageable): MutableIterable<Currency>
}
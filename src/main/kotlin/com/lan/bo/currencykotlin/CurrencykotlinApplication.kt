package com.lan.bo.currencykotlin

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.StandardEnvironment
import java.util.function.Function
import java.util.stream.Collectors


@SpringBootApplication
class CurrencykotlinApplication

fun main(args: Array<String>) {


	runApplication<CurrencykotlinApplication>(*args)
}

package com.fx.santander

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SantanderApplication

fun main(args: Array<String>) {
	runApplication<SantanderApplication>(*args)
}

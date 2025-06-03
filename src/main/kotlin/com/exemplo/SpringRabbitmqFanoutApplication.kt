package com.exemplo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringRabbitmqFanoutApplication

fun main(args: Array<String>) {
    runApplication<SpringRabbitmqFanoutApplication>(*args)
}
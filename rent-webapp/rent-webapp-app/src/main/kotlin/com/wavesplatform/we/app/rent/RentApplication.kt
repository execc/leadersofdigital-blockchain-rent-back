package com.wavesplatform.we.app.rent

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class RentApplication

fun main(args: Array<String>) {
    SpringApplication.run(RentApplication::class.java, *args)
}

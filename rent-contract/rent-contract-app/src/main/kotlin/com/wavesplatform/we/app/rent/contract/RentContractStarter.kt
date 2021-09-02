package com.wavesplatform.we.app.rent.contract

import com.wavesplatform.vst.contract.spring.annotation.EnableContractHandlers
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableContractHandlers
class RentContractStarter

fun main() {
    SpringApplication.run(RentContractStarter::class.java)
}
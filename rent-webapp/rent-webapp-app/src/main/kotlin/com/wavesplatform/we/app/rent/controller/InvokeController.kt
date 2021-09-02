package com.wavesplatform.we.app.rent.controller

import com.wavesplatform.we.app.rent.api.TxDto
import com.wavesplatform.we.app.rent.service.RentContractService
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public/invoke")
class InvokeController(
    val contractService: RentContractService
) {

    @PostMapping
    fun create(): ResponseEntity<TxDto> {
        val id = contractService.create()
        return ResponseEntity(TxDto(id), ACCEPTED)
    }

    @PostMapping("{contractId}/invoke")
    fun invoke(
        @PathVariable("contractId") contractId: String
    ): ResponseEntity<TxDto> {
        val id = contractService.invoke(contractId)
        return ResponseEntity(TxDto(id), ACCEPTED)
    }
}

package com.wavesplatform.we.app.rent.controller

import com.wavesplatform.we.app.rent.api.CreateRentalAgreementRq
import com.wavesplatform.we.app.rent.api.EarningRq
import com.wavesplatform.we.app.rent.api.TxDto
import com.wavesplatform.we.app.rent.domain.RentContractEntity
import com.wavesplatform.we.app.rent.service.RentContractService
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rent")
class RentalController(
    val contractService: RentContractService
) {

    @PostMapping
    fun create(@RequestBody rq: CreateRentalAgreementRq): ResponseEntity<TxDto> {
        val txId = contractService.create(rq)
        return ResponseEntity(TxDto(txId), ACCEPTED)
    }

    @PostMapping("/{id}/enterEarnings")
    fun enterEarnings(@PathVariable("id") id: String, @RequestBody rq: EarningRq): ResponseEntity<TxDto> {
        val txId = contractService.enterEarning(id, rq.amount)
        return ResponseEntity(TxDto(txId), ACCEPTED)
    }

    @GetMapping
    fun listAll(): List<RentContractEntity> {
        return contractService.listAll()
    }
}

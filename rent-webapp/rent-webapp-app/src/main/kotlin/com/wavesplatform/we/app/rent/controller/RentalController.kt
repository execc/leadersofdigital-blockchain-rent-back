package com.wavesplatform.we.app.rent.controller

import com.wavesplatform.we.app.rent.api.CreateRentalAgreementRq
import com.wavesplatform.we.app.rent.api.CreditAgreementRq
import com.wavesplatform.we.app.rent.api.EarningRq
import com.wavesplatform.we.app.rent.api.PayRq
import com.wavesplatform.we.app.rent.api.TxDto
import com.wavesplatform.we.app.rent.api.TxStatus
import com.wavesplatform.we.app.rent.api.TxStatusDto
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

    @PostMapping("/{id}/acceptCreditConditions")
    fun acceptCreditConditions(@PathVariable("id") id: String): ResponseEntity<TxDto> {
        val txId = contractService.acceptCreditConditions(id)
        return ResponseEntity(TxDto(txId), ACCEPTED)
    }

    @PostMapping("/{id}/enterCreditConditions")
    fun enterCreditConditions(@PathVariable("id") id: String, @RequestBody rq: CreditAgreementRq): ResponseEntity<TxDto> {
        val txId = contractService.enterCreditConditions(id, rq)
        return ResponseEntity(TxDto(txId), ACCEPTED)
    }

    @PostMapping("/{id}/takeRent")
    fun takeRent(@PathVariable("id") id: String): ResponseEntity<TxDto> {
        val txId = contractService.takeRent(id)
        return ResponseEntity(TxDto(txId), ACCEPTED)
    }

    @PostMapping("/{id}/payDebt")
    fun payDebt(@PathVariable("id") id: String, rq: PayRq): ResponseEntity<TxDto> {
        val txId = contractService.payDebt(id, rq)
        return ResponseEntity(TxDto(txId), ACCEPTED)
    }

    @PostMapping("/{id}/payCredit")
    fun payCredit(@PathVariable("id") id: String, rq: PayRq): ResponseEntity<TxDto> {
        val txId = contractService.payCredit(id, rq)
        return ResponseEntity(TxDto(txId), ACCEPTED)
    }

    @GetMapping("/tx/{id}/status")
    fun txStatus(@PathVariable("id") id: String): TxStatusDto {
        return TxStatusDto(contractService.txStatus(id))
    }

    @GetMapping
    fun listAll(): List<RentContractEntity> {
        return contractService.listAll()
    }
}

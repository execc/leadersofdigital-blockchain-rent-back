package com.wavesplatform.we.app.rent.config

import com.wavesplatform.we.app.rent.contract.RentContract
import com.wavesplatform.we.app.rent.contract.impl.RentContractImpl
import com.wavesplatform.we.starter.contract.annotation.Contract
import com.wavesplatform.we.starter.contract.annotation.EnableContracts
import org.springframework.context.annotation.Configuration

@Configuration
@EnableContracts(
        contracts = [Contract(
                api = RentContract::class,
                impl = RentContractImpl::class,
                name = "rent"
        )]
)
class RentContractConfig
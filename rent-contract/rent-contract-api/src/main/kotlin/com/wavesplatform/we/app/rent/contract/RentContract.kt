package com.wavesplatform.we.app.rent.contract

import com.wavesplatform.vst.contract.ContractAction
import com.wavesplatform.vst.contract.ContractInit

interface RentContract {

    @ContractInit
    fun create()

    @ContractAction
    fun invoke()
}

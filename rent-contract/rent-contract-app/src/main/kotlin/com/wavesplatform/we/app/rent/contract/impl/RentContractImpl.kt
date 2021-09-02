package com.wavesplatform.we.app.rent.contract.impl

import com.wavesplatform.vst.contract.spring.annotation.ContractHandlerBean
import com.wavesplatform.vst.contract.state.ContractState
import com.wavesplatform.vst.contract.state.setValue
import com.wavesplatform.vst.contract.state.getValue
import com.wavesplatform.we.app.rent.contract.RentContract

@ContractHandlerBean
class RentContractImpl(
    state: ContractState
) : RentContract {

    private var create: Boolean? by state
    private var invoke: Boolean? by state

    override fun create() {
        create = true
    }

    override fun invoke() {
        invoke = true
    }
}

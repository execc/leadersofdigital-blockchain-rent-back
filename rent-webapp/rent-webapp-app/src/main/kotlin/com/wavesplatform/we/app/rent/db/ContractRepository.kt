package com.wavesplatform.we.app.rent.db

import com.wavesplatform.we.app.rent.domain.RentContractEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ContractRepository : JpaRepository<RentContractEntity, String>

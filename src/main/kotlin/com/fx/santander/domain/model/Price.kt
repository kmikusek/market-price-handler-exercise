package com.fx.santander.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class Price(
    val id: Long,
    val instrument: Instrument,
    val bid: BigDecimal,
    val ask: BigDecimal,
    val timestamp: LocalDateTime
)

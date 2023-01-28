package com.fx.santander.domain.margin

import java.math.BigDecimal

interface MarginCalculator {

    fun getAskMargin(amount: BigDecimal): BigDecimal

    fun getBidMargin(amount: BigDecimal): BigDecimal
}
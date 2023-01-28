package com.fx.santander.domain.margin

import java.math.BigDecimal


private const val MARGIN_MULTIPLIER_IN_PERCENT = 0.1
private val MARGIN_MULTIPLIER_AS_BD = BigDecimal.valueOf(MARGIN_MULTIPLIER_IN_PERCENT)
private val ONE_HUNDRED = BigDecimal(100)


object FixedValueMarginMultiplier : MarginCalculator {

    override fun getAskMargin(amount: BigDecimal): BigDecimal =
        amount.multiply(MARGIN_MULTIPLIER_AS_BD)
            .divide(ONE_HUNDRED)

    override fun getBidMargin(amount: BigDecimal): BigDecimal =
        amount.multiply(MARGIN_MULTIPLIER_AS_BD)
            .divide(ONE_HUNDRED)
}
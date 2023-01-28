package com.fx.santander.domain.margin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class FixedValueMarginMultiplierTest {

    val calculator: MarginCalculator = FixedValueMarginMultiplier

    @Test
    fun `should return valid margin for ask`() {
        //given
        val value = BigDecimal.valueOf(1.200)
        //when
        val margin = calculator.getAskMargin(value)
        //then
        // 1.200 * 0,1% => 0.0012
        val expected = BigDecimal.valueOf(0.0012)
        assertEquals(margin, expected)
    }

    @Test
    fun `should return valid margin for bid`() {
        //given
        val value = BigDecimal.valueOf(1.2561)
        //when
        val margin = calculator.getBidMargin(value)
        //then
        // 1.2561 * 0,1% => 0.0012561
        val expected = BigDecimal.valueOf(0.0012561)
        assertEquals(margin, expected)
    }
}
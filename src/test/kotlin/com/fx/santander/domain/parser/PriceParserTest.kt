package com.fx.santander.domain.parser

import com.fx.santander.domain.model.Instrument
import com.fx.santander.domain.subscriber.PriceParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class PriceParserTest {

    private val pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")

    @Test
    fun `should return single price`() {
        //given
        val content = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001"
        //when
        val prices = PriceParser.parse(content)
        //then
        assert(prices.size == 1)
        val price = prices[0]
        assertEquals(106L, price.id)
        assertEquals(Instrument.EUR_USD, price.instrument)
        assert(BigDecimal.valueOf(1.1000).compareTo(price.bid) == 0)
        assert(BigDecimal.valueOf(1.2000).compareTo(price.ask) == 0)
        assert(LocalDateTime.parse("01-06-2020 12:01:01:001", pattern).isEqual(price.timestamp))
    }

    @Test
    fun `should return three prices`() {
        //given
        val content = "106, EUR/USD, 1.1000,1.2000,01-06-2020 12:01:01:001\n" +
                "107, EUR/JPY, 119.60,119.90,01-06-2020 12:01:02:002\n" +
                "108, GBP/USD, 1.2500,1.2560,01-06-2020 12:01:02:002"
        //when
        val prices = PriceParser.parse(content)
        //then
        assert(prices.size == 3)
        val price1 = prices[0]
        val price2 = prices[1]
        val price3 = prices[2]
        assertEquals(106L, price1.id)
        assertEquals(107L, price2.id)
        assertEquals(108L, price3.id)
        assertEquals(Instrument.EUR_USD, price1.instrument)
        assertEquals(Instrument.EUR_JPY, price2.instrument)
        assertEquals(Instrument.GBP_USD, price3.instrument)
        assert(BigDecimal.valueOf(1.1000).compareTo(price1.bid) == 0)
        assert(BigDecimal.valueOf(119.60).compareTo(price2.bid) == 0)
        assert(BigDecimal.valueOf(1.2500).compareTo(price3.bid) == 0)
        assert(BigDecimal.valueOf(1.2000).compareTo(price1.ask) == 0)
        assert(BigDecimal.valueOf(119.90).compareTo(price2.ask) == 0)
        assert(BigDecimal.valueOf(1.2560).compareTo(price3.ask) == 0)
        assert(LocalDateTime.parse("01-06-2020 12:01:01:001", pattern).isEqual(price1.timestamp))
        assert(LocalDateTime.parse("01-06-2020 12:01:02:002", pattern).isEqual(price2.timestamp))
        assert(LocalDateTime.parse("01-06-2020 12:01:02:002", pattern).isEqual(price3.timestamp))
    }
}
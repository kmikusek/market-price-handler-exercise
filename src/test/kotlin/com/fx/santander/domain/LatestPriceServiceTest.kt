package com.fx.santander.domain

import com.fx.santander.domain.margin.MarginCalculator
import com.fx.santander.domain.model.Instrument
import com.fx.santander.domain.model.Price
import com.fx.santander.repository.PriceRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime


@ExtendWith(MockitoExtension::class)
internal class LatestPriceServiceTest {

    @Mock
    lateinit var repository: PriceRepository

    @Mock
    lateinit var calculator: MarginCalculator

    private lateinit var service: LatestPriceService

    @BeforeEach
    fun setUp() {
        service = LatestPriceService(repository, calculator)
    }

    @Test
    fun `should calculate margin properly`() {
        //given
        val id = 99L
        val bid = BigDecimal.valueOf(1.25)
        val ask = BigDecimal.valueOf(1.35)
        val dt = LocalDateTime.now()
        val price = Price(id, Instrument.EUR_USD, bid, ask, dt)
        given(repository.getPrice(Instrument.EUR_USD)).willReturn(price)
        given(calculator.getBidMargin(bid)).willReturn(BigDecimal.valueOf(0.15))
        given(calculator.getAskMargin(ask)).willReturn(BigDecimal.valueOf(0.05))
        //when
        val result = service.getPriceSpot(Instrument.EUR_USD)
        //then
        assertNotNull(result)
        assertEquals(result!!.id, id)
        assert(result.bid.compareTo(BigDecimal.valueOf(1.10)) == 0)
        assert(result.ask.compareTo(BigDecimal.valueOf(1.40)) == 0)
        assertEquals(result.instrument, Instrument.EUR_USD)
        assertEquals(result.timestamp, dt)
    }

    @Test
    fun `should return null for invalid instrument`() {
        //given
        given(repository.getPrice(Instrument.GBP_USD)).willReturn(null)
        //when
        val result = service.getPriceSpot(Instrument.GBP_USD)
        //then
        assertEquals(result, null)
    }

    @Test
    fun `should set price for later timestamp`() {
        //given
        val id = 99L
        val newId = 33L
        val bid = BigDecimal.valueOf(1.10)
        val newBid = BigDecimal.valueOf(3.40)
        val ask = BigDecimal.valueOf(1.20)
        val newAsk = BigDecimal.valueOf(3.50)
        val dt = LocalDateTime.now()
        val newDt = LocalDateTime.now().plusHours(1)
        val price = Price(id, Instrument.GBP_USD, bid, ask, dt)
        given(repository.getPrice(Instrument.GBP_USD)).willReturn(price)
        //when
        val newPrice = Price(newId, Instrument.GBP_USD, newBid, newAsk, newDt)
        service.setPriceSpot(newPrice)
        //then
        verify(repository, times(1)).setPrice(newPrice)
    }

    @Test
    fun `shouldn't set price for legacy timestamp`() {
        //given
        val id = 99L
        val newId = 33L
        val bid = BigDecimal.valueOf(1.10)
        val newBid = BigDecimal.valueOf(3.40)
        val ask = BigDecimal.valueOf(1.20)
        val newAsk = BigDecimal.valueOf(3.50)
        val dt = LocalDateTime.now()
        val newDt = LocalDateTime.now().minusHours(1)
        val price = Price(id, Instrument.GBP_USD, bid, ask, dt)
        given(repository.getPrice(Instrument.GBP_USD)).willReturn(price)
        //when
        val newPrice = Price(newId, Instrument.GBP_USD, newBid, newAsk, newDt)
        service.setPriceSpot(newPrice)
        //then
        verify(repository, times(0)).setPrice(newPrice)
    }
}
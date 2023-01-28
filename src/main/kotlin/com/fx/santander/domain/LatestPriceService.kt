package com.fx.santander.domain

import com.fx.santander.domain.margin.MarginCalculator
import com.fx.santander.domain.model.Instrument
import com.fx.santander.domain.model.Price
import com.fx.santander.repository.PriceRepository
import org.springframework.stereotype.Service


@Service
class LatestPriceService(
    private val repository: PriceRepository,
    private val calculator: MarginCalculator
) {

    fun getPriceSpot(instrument: Instrument) : Price? {
        repository.getPrice(instrument)?.let { price ->
            val bidMargin = calculator.getBidMargin(price.bid)
            val askMargin = calculator.getAskMargin(price.ask)
            return Price(
                price.id,
                price.instrument,
                price.bid - bidMargin,
                price.ask + askMargin,
                price.timestamp
            )
        } ?: return null
    }

    fun setPriceSpot(price: Price) {
        repository.getPrice(price.instrument)?.let { oldPrice ->
            if (oldPrice.timestamp.isBefore(price.timestamp)) {
                repository.setPrice(price)
            }
        } ?: repository.setPrice(price)
    }
}
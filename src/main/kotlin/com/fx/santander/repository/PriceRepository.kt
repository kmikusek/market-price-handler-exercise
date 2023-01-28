package com.fx.santander.repository

import com.fx.santander.domain.model.Instrument
import com.fx.santander.domain.model.Price
import org.springframework.stereotype.Repository


@Repository
class PriceRepository {

    private val items = mutableMapOf<Instrument, Price>()

    fun setPrice(price: Price) {
        items[price.instrument] = price
    }

    fun getPrice(instrument: Instrument): Price? =
        items[instrument]
}
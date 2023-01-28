package com.fx.santander.domain.subscriber

import com.fx.santander.domain.model.Instrument
import com.fx.santander.domain.model.Price
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PriceParser {

    private val pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS")

    fun parse(content: String): List<Price> {
        val items = mutableListOf<Price>()
        try {
            content.split('\n').forEach {
                val fx = it.split(',')
                if (fx.size != 5) throw UnsupportedOperationException("this operation cannot be performed")
                val id = fx[0].replace(" ", "").toLong()
                val instrument = Instrument.create(fx[1].replace(" ", ""))
                val bid = fx[2].replace(" ", "").toBigDecimal()
                val ask = fx[3].replace(" ", "").toBigDecimal()
                val timestamp = LocalDateTime.parse(fx[4], pattern)
                items.add(
                    Price(id, instrument, bid, ask, timestamp)
                )
            }
        } catch (e:Exception) {
            //FIXME these exceptions should be handled in some way; depending on the architecture - DLQ?
            throw UnsupportedOperationException("this operation cannot be performed")
        }
        return items
    }
}
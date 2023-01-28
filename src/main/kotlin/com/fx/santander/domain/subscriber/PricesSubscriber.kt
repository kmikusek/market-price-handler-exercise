package com.fx.santander.domain.subscriber

import com.fx.santander.domain.LatestPriceService
import org.springframework.stereotype.Component


@Component
class PricesSubscriber(
    private val service: LatestPriceService
)  {

    fun onMessage(data: String) {
        println("Listener received: $data")
        val prices = PriceParser.parse(data)
        prices.forEach {
            service.setPriceSpot(it)
        }
    }
}
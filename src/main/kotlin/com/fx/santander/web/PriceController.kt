package com.fx.santander.web

import com.fx.santander.domain.model.Instrument
import com.fx.santander.domain.model.Price
import com.fx.santander.domain.LatestPriceService
import com.fx.santander.exceptions.NotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class PriceController(
    private val service: LatestPriceService
) {

    @GetMapping("/v1/price/{instrument}")
    fun getPrice(@PathVariable instrument: Instrument): Price =
        service.getPriceSpot(instrument)?.let {
            return it
        } ?: throw NotFoundException(instrument.toString())
}


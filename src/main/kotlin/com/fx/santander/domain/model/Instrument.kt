package com.fx.santander.domain.model

enum class Instrument {
    EUR_USD,
    EUR_JPY,
    GBP_USD;

    override fun toString(): String {
        return name.replace("_", "/")
    }

    companion object Factory {
        fun create(string: String): Instrument =
            valueOf(string.replace("/", "_"))
    }
}
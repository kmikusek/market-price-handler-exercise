package com.fx.santander.config

import com.fx.santander.domain.margin.FixedValueMarginMultiplier
import com.fx.santander.domain.margin.MarginCalculator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MarginConfig {

    @Bean
    fun provideMarginImpl(): MarginCalculator =
        FixedValueMarginMultiplier
}
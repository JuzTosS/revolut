package com.juztoss.revolut.api.responces

import com.juztoss.revolut.data.Currency

data class RatesResponse(
        val base: Currency,
        val rates: LinkedHashMap<String, Double>
)
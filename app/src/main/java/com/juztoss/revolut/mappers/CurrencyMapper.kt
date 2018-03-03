package com.juztoss.revolut.mappers

import com.juztoss.revolut.api.responces.RatesResponse
import com.juztoss.revolut.data.Currency
import com.juztoss.revolut.data.CurrencyValue

fun map(response: RatesResponse): Map<Currency, Double> {
    return response.rates.mapKeys {
        Currency.valueOf(it.key)
    }
}
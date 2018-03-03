package com.juztoss.revolut.data

enum class Currency {
    AUD,
    BGN,
    BRL,
    CAD,
    CHF,
    CNY,
    CZK,
    DKK,
    EUR,
    GBP,
    HKD,
    HRK,
    HUF,
    IDR,
    ILS,
    INR,
    ISK,
    JPY,
    KRW,
    MXN,
    MYR,
    NOK,
    NZD,
    PHP,
    PLN,
    RON,
    RUB,
    SEK,
    SGD,
    THB,
    TRY,
    USD,
    ZAR,
}

data class CurrencyValue(
        val currency: Currency,
        val value: Double
)
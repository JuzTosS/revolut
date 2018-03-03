package com.juztoss.revolut.screens

import com.hannesdorfmann.mosby.mvp.MvpView
import com.juztoss.revolut.data.Currency
import com.juztoss.revolut.data.CurrencyValue

interface CurrenciesListView : MvpView {
    fun updateValues()
    fun updateList(data: List<Currency>, scrollToTop:Boolean)
    fun onError(t: Throwable)
}

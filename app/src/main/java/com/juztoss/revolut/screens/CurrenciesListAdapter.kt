package com.juztoss.revolut.screens

import android.view.LayoutInflater
import android.view.ViewGroup
import com.juztoss.revolut.R
import com.juztoss.revolut.data.Currency
import com.juztoss.revolut.data.CurrencyValue
import com.juztoss.revolut.view.DiffRecyclerViewAdapter

interface CurrencyValueProvider {
    fun calculateValue(currency: Currency): Double
    fun changeBase(value: Double)
    fun setUpdatesListener(onUpdate: () -> Unit)
}

class CurrenciesListAdapter(
        private val onCick: (CurrencyValue) -> Unit,
        private val valueProvider: CurrencyValueProvider
) : DiffRecyclerViewAdapter<Currency, CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_currency, parent, false)
        return CurrencyViewHolder(view, onCick, valueProvider)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.update(data[position])
    }
}

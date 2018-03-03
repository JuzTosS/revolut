package com.juztoss.revolut.screens

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import com.juztoss.revolut.R
import com.juztoss.revolut.data.Currency
import com.juztoss.revolut.data.CurrencyValue
import kotlinx.android.synthetic.main.item_currency.view.*

class CurrencyViewHolder(
        view: View,
        private val onCick: (CurrencyValue) -> Unit,
        private val valueProvider: CurrencyValueProvider
) : RecyclerView.ViewHolder(view) {

    private lateinit var currency: Currency

    init {
        view.setOnClickListener {
            onCick(CurrencyValue(currency, getValue()))
            itemView.valueText.requestFocus()
        }

        valueProvider.setUpdatesListener { updateValue() }

        itemView.valueText.setUserInputListener {
            if (itemView.valueText.isFocused && adapterPosition == 0) {
                valueProvider.changeBase(getValue())
            }
        }

        itemView.valueText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                onCick(CurrencyValue(currency, getValue()))
            }
        }
    }

    private fun getValue(): Double {
        try {
            return itemView.valueText.text.toString().toDouble()
        } catch (e: NumberFormatException) {
            return 0.0
        }
    }

    private fun getDrawableId(currency: Currency): Drawable? {
        val drawableId: Int = when (currency) {
            Currency.AUD -> R.drawable.flag_au
            Currency.BGN -> R.drawable.flag_bg
            Currency.BRL -> R.drawable.flag_bg
            Currency.CAD -> R.drawable.flag_ca
            Currency.CHF -> R.drawable.flag_ch
            Currency.CNY -> R.drawable.flag_cn
            Currency.CZK -> R.drawable.flag_cz
            Currency.DKK -> R.drawable.flag_dk
            Currency.EUR -> R.drawable.flag_bb
            Currency.GBP -> R.drawable.flag_gp
            Currency.HKD -> R.drawable.flag_hk
            Currency.HRK -> R.drawable.flag_hm
            Currency.HUF -> R.drawable.flag_hu
            Currency.IDR -> R.drawable.flag_id
            Currency.ILS -> R.drawable.flag_il
            Currency.INR -> R.drawable.flag_in
            Currency.ISK -> R.drawable.flag_is
            Currency.JPY -> R.drawable.flag_jp
            Currency.KRW -> R.drawable.flag_kr
            Currency.MXN -> R.drawable.flag_bg
            Currency.MYR -> R.drawable.flag_my
            Currency.NOK -> R.drawable.flag_no
            Currency.NZD -> R.drawable.flag_nz
            Currency.PHP -> R.drawable.flag_ph
            Currency.PLN -> R.drawable.flag_pl
            Currency.RON -> R.drawable.flag_ro
            Currency.RUB -> R.drawable.flag_ru
            Currency.SEK -> R.drawable.flag_se
            Currency.SGD -> R.drawable.flag_sg
            Currency.THB -> R.drawable.flag_th
            Currency.TRY -> R.drawable.flag_tr
            Currency.USD -> R.drawable.flag_us
            Currency.ZAR -> R.drawable.flag_za
        }

        if (drawableId == 0) {
            return null
        } else {
            return itemView.context.getDrawable(drawableId)
        }
    }

    fun update(currency: Currency) {
        this.currency = currency
        updateValue()
        itemView.nameText.setText(currency.toString())
        itemView.flagIcon.setImageDrawable(getDrawableId(currency))
    }

    private fun updateValue() {
        val value = valueProvider.calculateValue(currency)
        //Avoid reseting the cursor position
        if (value != getValue() || itemView.valueText.text.isEmpty()) {
            itemView.valueText.setText(String.format(itemView.context.getString(R.string.currency_format), value))
        }
    }
}

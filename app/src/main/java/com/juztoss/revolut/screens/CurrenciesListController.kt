package com.juztoss.revolut.screens

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.juztoss.revolut.R
import com.juztoss.revolut.data.Currency
import com.juztoss.revolut.data.CurrencyValue
import com.juztoss.revolut.mvp.MvpBaseController
import kotlinx.android.synthetic.main.currencies_list_controller.view.*

class CurrenciesListController
    : MvpBaseController<CurrenciesListView, CurrenciesListPresenter>,
        CurrenciesListView,
        CurrencyValueProvider {

    private val adapter: CurrenciesListAdapter = CurrenciesListAdapter(this::onClick, this)
    private val updatesListeners: MutableList<() -> Unit> = mutableListOf()

    constructor() : super()
    constructor(args: Bundle) : super(args)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.currencies_list_controller, container, false)
        view.list.adapter = adapter
        return view;
    }

    override fun createNewPresenter(): CurrenciesListPresenter {
        return CurrenciesListPresenter()
    }

    private fun onClick(value: CurrencyValue) {
        presenter.updateSelected(value)
    }

    override fun onError(t: Throwable) {
        Toast.makeText(activity, t.message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityPaused(activity: Activity) {
        presenter?.onPausePolling()
    }

    override fun onActivityResumed(activity: Activity) {
        presenter?.onResumePolling()
    }

    override fun updateList(data: List<Currency>, scrollToTop:Boolean) {
        adapter.update(data, scrollToTop, view!!.list)
    }

    override fun calculateValue(currency: Currency): Double {
        return presenter.calculateValue(currency)
    }

    override fun changeBase(value: Double) {
        presenter.changeBase(value)
    }

    override fun setUpdatesListener(onUpdate: () -> Unit) {
        updatesListeners.add(onUpdate)
    }

    override fun updateValues() {
        updatesListeners.forEach {
            it()
        }
    }
}

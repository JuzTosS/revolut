package com.juztoss.revolut.screens

import com.juztoss.revolut.api.ApiService
import com.juztoss.revolut.data.Currency
import com.juztoss.revolut.data.CurrencyValue
import com.juztoss.revolut.di.Injector
import com.juztoss.revolut.mappers.map
import com.juztoss.revolut.mvp.MvpBasePresenter
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val POLL_INTERVAL_SEC = 1L

class CurrenciesListPresenter : MvpBasePresenter<CurrenciesListView>() {

    @Inject lateinit var api: ApiService

    private var disposable: Disposable? = null

    @Volatile
    private var selectedCurrency: Currency = Currency.EUR

    @Volatile
    private var base: Double = 1.0

    private var rates: Map<Currency, Double>? = null

    private var requestLauncher: PublishSubject<Long> = PublishSubject.create()

    private var scrollToTopNeeded = false

    override fun inject() {
        Injector.getAppComponent().inject(this)
    }

    override fun init() {
    }

    private fun makeRequest() {
        requestLauncher.onNext(0)
    }

    override fun attachView(v: CurrenciesListView) {
        super.attachView(v)
        onResumePolling()
        makeRequest()
    }

    override fun detachView(retainInstance: Boolean) {
        onPausePolling()
    }

    fun onPausePolling() {
        disposable?.dispose()
        disposable = null
    }

    /**
     * Starts polling rates with POLL_INTERVAL_SEC interval;
     * Allow to make an additional request by emitting a value on requestLauncher
     */
    fun onResumePolling() {
        if (disposable == null) {
            disposable = Flowable.interval(POLL_INTERVAL_SEC, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .mergeWith(requestLauncher.toFlowable(BackpressureStrategy.DROP))
                    .onBackpressureDrop()
                    .concatMap {
                        api.phoneTypes(selectedCurrency.name)
                                .map { map(it) }
                                .toFlowable()
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext { rates = it }
                    .observeOn(Schedulers.io())
                    .map { ratesToList(it, selectedCurrency) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { data ->
                                if (scrollToTopNeeded) {
                                    view.updateList(data, true)
                                    scrollToTopNeeded = false
                                } else {
                                    view.updateList(data, false)
                                }
                                view.updateValues()
                            },
                            view::onError
                    )

        }
    }

    /**
     * Changes the base currency and updates rates
     */
    fun updateSelected(currencyValue: CurrencyValue) {
        selectedCurrency = currencyValue.currency
        base = currencyValue.value
        scrollToTopNeeded = true
        makeRequest()
    }

    private fun ratesToList(rates: Map<Currency, Double>, selected: Currency): List<Currency> {
        val newData = mutableListOf(selected)
        newData.addAll(
                rates.filter { it.key != selected }
                        .map { it.key }
        )
        return newData
    }

    /**
     * Changes the amount of base currency
     */
    fun changeBase(value: Double) {
        base = value
        view.updateValues()
    }

    /**
     * Returns the currency value based on downloaded rates and the amount of base currency
     */
    fun calculateValue(currency: Currency): Double {
        val rates = rates
        if (rates != null) {
            val rate = rates.get(currency)
            if (rate != null) {
                return rate * base
            } else {
                return base
            }
        }
        return 0.0
    }
}

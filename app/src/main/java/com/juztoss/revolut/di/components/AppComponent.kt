package com.juztoss.revolut.di.components

import com.juztoss.revolut.di.modules.AppModule
import com.juztoss.revolut.di.modules.NetworkModule
import com.juztoss.revolut.screens.CurrenciesListPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class
])
interface AppComponent {
    fun inject(currenciesListPresenter: CurrenciesListPresenter)
}
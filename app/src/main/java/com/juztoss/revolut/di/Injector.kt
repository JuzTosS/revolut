package com.juztoss.revolut.di

import com.juztoss.revolut.App
import com.juztoss.revolut.di.components.AppComponent
import com.juztoss.revolut.di.components.DaggerAppComponent
import com.juztoss.revolut.di.modules.AppModule

object Injector {
    private lateinit var component: AppComponent

    fun prepare(application: App) {
        component = DaggerAppComponent.builder()
                .appModule(AppModule(application))
                .build()
    }

    fun getAppComponent() = component
}
package com.juztoss.revolut

import android.app.Application
import android.util.Log
import com.juztoss.revolut.di.Injector
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Injector.prepare(this)
        RxJavaPlugins.setErrorHandler { Log.e(RxJavaPlugins::class.java.name, "Uncaught Rx error", it) }
    }
}

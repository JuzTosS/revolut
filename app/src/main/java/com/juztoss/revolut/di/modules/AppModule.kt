package com.juztoss.revolut.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return appContext
    }
}

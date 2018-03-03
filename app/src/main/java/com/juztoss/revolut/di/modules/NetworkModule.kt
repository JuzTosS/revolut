package com.juztoss.revolut.di.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.juztoss.revolut.R
import com.juztoss.revolut.api.ApiService
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson, context: Context): Retrofit {
        return Retrofit.Builder()
                .baseUrl(context.getString(R.string.endpoint))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

}

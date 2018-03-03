package com.juztoss.revolut.api

import com.juztoss.revolut.api.responces.RatesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val LATEST_RATES = "/latest"

interface ApiService {

    @GET(LATEST_RATES)
    fun phoneTypes(@Query("base") currency: String): Single<RatesResponse>
}

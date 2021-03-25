package com.abunayla.simpleretrofitexample.api

import retrofit2.Call
import retrofit2.http.GET


interface ApiRequests {

    @GET(value = "/facts/random")
    fun getCatFacts(): Call<CatsJson>
}
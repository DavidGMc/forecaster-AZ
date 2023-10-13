package com.androidavid.credibanco.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val paymentService: ApiWeatherService by lazy {
        retrofit.create(ApiWeatherService::class.java)
    }
}
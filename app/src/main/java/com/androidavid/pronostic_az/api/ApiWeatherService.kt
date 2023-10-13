package com.androidavid.credibanco.api

import com.androidavid.pronostic_az.model.ForecastModel.ForecastResponse
import com.androidavid.pronostic_az.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiWeatherService {

    @GET("weather")
   fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("lang") lang: String = "es" // Agrega el parámetro 'lang' para español
    ): Call<WeatherModel>

    @GET("forecast")
  fun getDailyWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("cnt") count: Int, // Número de días (5 para los próximos 5 días)
        @Query("APPID") appid: String,
        @Query("lang") lang: String = "es" // Agrega el parámetro 'lang' para español
    ): Call<ForecastResponse>





}
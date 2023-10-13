package com.androidavid.pronostic_az.ForecastModel

data class ForecastResponse(
    val city: City,
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>
)

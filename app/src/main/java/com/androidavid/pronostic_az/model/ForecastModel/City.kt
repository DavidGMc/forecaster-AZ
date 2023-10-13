package com.androidavid.pronostic_az.model.ForecastModel

import com.androidavid.pronostic_az.model.Coord

data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int
)

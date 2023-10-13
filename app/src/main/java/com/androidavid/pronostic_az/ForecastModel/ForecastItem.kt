package com.androidavid.pronostic_az.ForecastModel

import com.androidavid.pronostic_az.model.Clouds
import com.androidavid.pronostic_az.model.Main
import com.androidavid.pronostic_az.model.Sys
import com.androidavid.pronostic_az.model.Weather
import com.androidavid.pronostic_az.model.Wind

data class ForecastItem(
    val dt: Long, // Timestamp del pronóstico
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double, // Probabilidad de precipitación
    val sys: Sys,
    val dt_txt: String // Fecha y hora en formato de texto
)

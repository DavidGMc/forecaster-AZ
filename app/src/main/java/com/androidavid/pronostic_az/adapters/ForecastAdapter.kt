package com.androidavid.pronostic_az.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androidavid.pronostic_az.R
import com.androidavid.pronostic_az.model.ForecastModel.ForecastItem

class ForecastAdapter(private val forecastData: List<ForecastItem>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ForecastViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecastItem = forecastData[position]


        val temperatureCelsius = forecastItem.main.temp - 273.15

        holder.forecastIconImageView.setImageResource(getWeatherIconResourceId(forecastItem.weather[0].icon))
        holder.forecastDateTextView.text = forecastItem.dt_txt
        holder.forecastTemperatureTextView.text = "${String.format("%.1f", temperatureCelsius)}°C"
        holder.forecastDescriptionTextView.text = forecastItem.weather[0].description
        val tempMaxCelsius = forecastItem.main.temp_max - 273.15
        val tempMinCelsius = forecastItem.main.temp_min - 273.15

        holder.forecastTempMaxTextView.text = "Temp Max: ${String.format("%.1f", tempMaxCelsius)}°C"
        holder.forecastTempMinTextView.text = "Temp Min: ${String.format("%.1f", tempMinCelsius)}°C"

    }

    override fun getItemCount(): Int {
        return forecastData.size
    }

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val forecastIconImageView: ImageView = itemView.findViewById(R.id.forecastIconImageView)
        val forecastDateTextView: TextView = itemView.findViewById(R.id.forecastDateTextView)
        val forecastTemperatureTextView: TextView = itemView.findViewById(R.id.forecastTemperatureTextView)
        val forecastDescriptionTextView: TextView = itemView.findViewById(R.id.forecastDescriptionTextView)
        val forecastTempMaxTextView: TextView = itemView.findViewById(R.id.forecastTempMaxTextView)
        val forecastTempMinTextView: TextView = itemView.findViewById(R.id.forecastTempMinTextView)
    }

    private fun getWeatherIconResourceId(iconName: String): Int {
        // Mapea los recursos de íconos

        return when (iconName) {
            "01d" -> R.drawable.oned
            "01n" -> R.drawable.onen
            "02d" -> R.drawable.twod
            "02n" -> R.drawable.twon
            "03d", "03n" -> R.drawable.threedn
            "04d", "04n" -> R.drawable.fourdn
            "09d", "09n" -> R.drawable.ninedn
            "10d" -> R.drawable.tend
            "10n" -> R.drawable.tenn
            "11d", "11n" -> R.drawable.elevend
            "13d", "13n" -> R.drawable.thirteend
            "50d", "50n" -> R.drawable.fiftydn
            else -> R.drawable.warning
        }
    }
}
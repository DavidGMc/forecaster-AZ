package com.androidavid.pronostic_az

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidavid.credibanco.api.ApiClient
import com.androidavid.credibanco.api.ApiWeatherService
import com.androidavid.pronostic_az.databinding.ActivityMainBinding
import com.androidavid.pronostic_az.model.WeatherModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidavid.pronostic_az.ForecastModel.ForecastResponse

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var apiInterface: ApiWeatherService

    private lateinit var temperatureTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var weatherIconImageView: ImageView
    private lateinit var updateWeatherButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        temperatureTextView = findViewById(R.id.temperatureTextView)
        descriptionTextView = findViewById(R.id.weatherDescriptionTextView)
        weatherIconImageView = findViewById(R.id.weatherIconImageView)
        updateWeatherButton = binding.updateWeatherButton


        val forecastRecyclerView = findViewById<RecyclerView>(R.id.forecastRecyclerView)
        forecastRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        updateWeatherButton.setOnClickListener {
            getLastKnownLocation()

        }

        // Inicializa el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Inicializa la interfaz de la API (asegúrate de haber configurado correctamente la URL base y la clave de API)
        apiInterface = ApiClient.paymentService

        if (checkLocationPermission()) {
            // Si ya se tienen los permisos, puedes continuar con la lógica para obtener la ubicación.
            getLastKnownLocation()
        } else {
            // Si no se tienen los permisos, solicitarlos al usuario.
            requestLocationPermission()
        }






    }


    private fun checkLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocationPermission || coarseLocationPermission
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // El usuario concedió permisos, puedes continuar con la lógica para obtener la ubicación.
                    getLastKnownLocation()
                } else {
                    // El usuario denegó los permisos, debes manejar esto adecuadamente, por ejemplo, mostrando un mensaje.
                }
            }
        }
    }

    private fun getLastKnownLocation() {
        // Verificar nuevamente si se tienen los permisos de ubicación
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    // La ubicación se obtuvo con éxito, puedes acceder a las coordenadas geográficas desde 'location'.
                    val latitude = location?.latitude
                    val longitude = location?.longitude

                    if (latitude != null && longitude != null) {
                        // Si se obtuvieron las coordenadas, solicita el pronóstico del clima
                        getCurrentWeatherByLocation(latitude.toString(), longitude.toString())
                        get5DayForecastByLocation(latitude.toString(), longitude.toString())
                    }
                }
        } else {
            // Si no se tienen los permisos, solicitarlos nuevamente.
            requestLocationPermission()
        }
    }
    private fun getCurrentWeatherByLocation(latitude: String, longitude: String) {

        val apiKey = "3933fc15820143a022522600fd31c7ee"
        val call = apiInterface.getCurrentWeatherData(latitude, longitude, apiKey)



            call.enqueue(object : Callback<WeatherModel> {
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            // Accede a los datos del pronóstico del clima
                            val temperatureFahrenheit = weatherData!!.main?.temp
                            val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
                            val temperatureFormatted = String.format("%.2f", temperatureCelsius)

                            val temperature = "$temperatureFormatted°"
                            val description = weatherData.weather[0].description
                            val weatherIcon = weatherData.weather[0].icon
                            val cityName = weatherData.name


                            // Actualiza la interfaz de usuario con los datos del clima
                            updateUI(temperature, description, weatherIcon, cityName)
                        }
                    } else {
                        // Maneja los errores de la respuesta de la API aquí
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    // Maneja los errores de la solicitud aquí
                }
            })

    }

    private fun get5DayForecastByLocation(latitude: String, longitude: String) {
        val apiKey = "3933fc15820143a022522600fd31c7ee" // Reemplaza con tu clave de API de OpenWeatherMap
        val count = 40 // Obtener el pronóstico para los próximos 5 días

        val call = apiInterface.getDailyWeatherForecast(latitude, longitude, count, apiKey)

        call.enqueue(object : Callback<ForecastResponse> {
            override fun onResponse(call: Call<ForecastResponse>, response: Response<ForecastResponse>) {
                if (response.isSuccessful) {
                    val forecastData = response.body()
                    if (forecastData != null) {
                        // pronóstico de 5 días/3 horas
                        val dailyForecasts = forecastData.list

                        val forecastRecyclerView = findViewById<RecyclerView>(R.id.forecastRecyclerView)

                        val forecastAdapter = ForecastAdapter(dailyForecasts) // Pasa los datos del pronóstico
                        forecastRecyclerView.adapter = forecastAdapter
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<ForecastResponse>, t: Throwable) {

            }
        })
    }


    // Actualizar la interfaz de usuario con los datos del clima
    private fun updateUI(temperature: String, description: String, weatherIcon: String, city:String) {

        temperatureTextView.text = temperature
        descriptionTextView.text = description
        binding.cityTextView.text= city
       weatherIconImageView.setImageResource(getWeatherIconResource(weatherIcon))

    }
    private fun getWeatherIconResource(weatherIcon: String): Int {
        return when (weatherIcon) {
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
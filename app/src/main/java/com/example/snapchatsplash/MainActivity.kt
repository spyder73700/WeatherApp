package com.example.snapchatsplash
// a9ae6af519a095c056804a940cf905fc
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.snapchatsplash.databinding.ActivityMainBinding
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val dayTextView = findViewById<TextView>(R.id.Day)
        val dateTextView = findViewById<TextView>(R.id.Date)

        // Get current date and day
        val calendar = Calendar.getInstance()

        // Format day (e.g., Monday, Tuesday)
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val day = dayFormat.format(calendar.time)

        // Format date (e.g., 25 January 2024)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = dateFormat.format(calendar.time)

        // Set the day and date in TextViews
        dayTextView.text = day
        dateTextView.text = date
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Play Animation Programmatically
        lottieAnimationView.setAnimation(R.raw.sun) // Replace with your animation file
        lottieAnimationView.playAnimation()




        val searchView = findViewById<SearchView>(R.id.searchview)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetachWeatherData(it) // Call API with user input
                    searchView.clearFocus()
                }
                return true


            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        fetachWeatherData("jaipur")

    }
    fun fetachWeatherData(cityName: String) {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response = retrofit.getWeatherData(
            city = cityName,
            appid = "a9ae6af519a095c056804a940cf905fc",
            units = "metric"
        )

        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                // Handle successful response
                val responseBody = response.body()
                if (response.isSuccessful) {

                    val temperature = responseBody?.main?.temp ?: 0.0 // Default value if null
                    val formattedTemp = String.format("%.2f°C", temperature)
                    val humidity = responseBody?.main?.humidity
                    val windSpeed = responseBody?.wind?.speed
                    val sunRise = responseBody?.sys?.sunrise // Fetch sunrise time
                    val sunSet = responseBody?.sys?.sunset   // Fetch sunset time
                    val sunriseTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(Date(sunRise?.toLong()?.times(1000) ?: 0))

                    val sunsetTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        .format(Date(sunSet?.toLong()?.times(1000) ?: 0))
                    val seaLevel = responseBody?.main?.pressure
                    val condition = responseBody?.weather?.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody?.main?.temp_max
                    val minTemp = responseBody?.main?.temp_min


                    runOnUiThread {
                        binding.temp.text = formattedTemp
                        binding.maxTemp.text = "Max Temp: $maxTemp°C"
                        binding.minTemp.text = "Min Temp: $minTemp°C"
                        binding.humidity.text = "$humidity %"
                        binding.windspeed.text = "$windSpeed m/s"
                        binding.sunrise.text = "$sunriseTime"
                        binding.sunset.text = "$sunsetTime"
                        binding.sea.text = "$seaLevel hPa"
                        binding.conditions.text = condition
                        binding.textView5.text = condition
                        binding.cityName.text = cityName
                    }
                    val layout = findViewById<ConstraintLayout>(R.id.main)
                    val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
                    // Set background based on condition
                    when (condition.lowercase()) {
                        "clear" -> layout.setBackgroundResource(R.drawable.sunny_background) // sunny
                        "clouds" -> layout.setBackgroundResource(R.drawable.colud_background) // cloudy
                        "rain" -> layout.setBackgroundResource(R.drawable.rain_background) // rainy
                        "snow" -> layout.setBackgroundResource(R.drawable.snow_background) // snowy
                        else -> layout.setBackgroundResource(R.drawable.sunny_background) // default
                    }
                    when (condition.lowercase()) {
                        "clear" -> lottieAnimationView.setAnimation(R.raw.sun) // Sunny animation
                        "clouds" -> lottieAnimationView.setAnimation(R.raw.cloud) // Cloudy animation
                        "rain" -> lottieAnimationView.setAnimation(R.raw.rain) // Rainy animation
                        "snow" -> lottieAnimationView.setAnimation(R.raw.snow) // Snowy animation
                       else -> lottieAnimationView.setAnimation(R.raw.sun) // Default animation
                    }
                    lottieAnimationView.playAnimation()


                    //Log.d("ashish", "Temperature: $formattedTemp")






                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                // Handle failure
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}

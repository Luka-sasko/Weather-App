package com.example.weatherapiapp

import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val day1Image=findViewById<ImageView>(R.id.Day1ImageView)
        val day2Image=findViewById<ImageView>(R.id.Day2ImageView)
        val day3Image=findViewById<ImageView>(R.id.Day3ImageView)


        fun parseJsonToWeatherData(jsonString: String): WeatherData? {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            val adapter: JsonAdapter<WeatherData> = moshi.adapter(WeatherData::class.java)

            return try {
                adapter.fromJson(jsonString)
            } catch (e: Exception) {
                // Handle parsing exceptions here
                e.printStackTrace()
                null
            }
        }

        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://weatherapi-com.p.rapidapi.com/forecast.json?q=Osijek&days=3")
            .get()
            .addHeader("X-RapidAPI-Key", "f9a73f3270mshf93ddb506a6e480p1b9446jsnd1d4bfc14a1a")
            .addHeader("X-RapidAPI-Host", "weatherapi-com.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {

                    val responseBody = response.body?.string()
                    val weatherData = parseJsonToWeatherData(responseBody.toString().trimIndent())
                    val adapterWeather=WeatherAdapter(weatherData!!)
                    weatherData.forecast.forecastday[0].day.daily_chance_of_rain

                    Log.i("weatherData",weatherData!!.current.condition.toString())

                    val day1ChanceOfRain=findViewById<TextView>(R.id.day1TVChanceOfRain)
                    val day2ChanceOfRain=findViewById<TextView>(R.id.day2TVChanceOfRain)
                    val day3ChanceOfRain=findViewById<TextView>(R.id.day3TVChanceOfRain)

                    val day1Condition=findViewById<TextView>(R.id.day1Condition)
                    val day2Condition=findViewById<TextView>(R.id.day2Condition)
                    val day3Condition=findViewById<TextView>(R.id.day3Condition)

                    val day1Date=findViewById<TextView>(R.id.day1TvDay)
                    val day2Date=findViewById<TextView>(R.id.day2TvDay)
                    val day3Date=findViewById<TextView>(R.id.day3TvDay)

                    val day1MaxTemp=findViewById<TextView>(R.id.day1TVTemp)
                    val day2MaxTemp=findViewById<TextView>(R.id.day2TVTemp)
                    val day3MaxTemp=findViewById<TextView>(R.id.day3TVTemp)

                    var DaysIcons:MutableList<String> = mutableListOf()
                    var DaysDates:MutableList<String> = mutableListOf()
                    var DaysConditions:MutableList<String> = mutableListOf()
                    var DaysMaxTemps:MutableList<String> = mutableListOf()
                    var DaysMinTemps:MutableList<String> = mutableListOf()
                    var DaysChanceOfRain:MutableList<String> = mutableListOf()


                    DaysConditions=adapterWeather.GetConditionText()
                    DaysIcons=adapterWeather.GetDaysIcons()
                    DaysDates=adapterWeather.GetDaysDate()
                    DaysMaxTemps=adapterWeather.GetMaxTemps()
                    DaysMinTemps=adapterWeather.GetMinTemps()
                    DaysChanceOfRain=adapterWeather.GetChanceOfRain()

                    //Postavljanje ChanceOfRain na svaki dan
                    runOnUiThread {
                        day1ChanceOfRain.text = DaysChanceOfRain[0]
                        day2ChanceOfRain.text = DaysChanceOfRain[1]
                        day3ChanceOfRain.text = DaysChanceOfRain[2]
                    }

                    //Postavljanje MaxTemp + MinTemp na svaki dan
                    runOnUiThread {
                        day1MaxTemp.text = DaysMaxTemps[0]+" | "+DaysMinTemps[0]
                        day2MaxTemp.text = DaysMaxTemps[1]+" | "+DaysMinTemps[1]
                        day3MaxTemp.text = DaysMaxTemps[2]+" | "+DaysMinTemps[2]
                    }

                        //Postavljanje Condition na svaki dan
                    runOnUiThread {
                        day1Condition.text = DaysConditions[0]
                        day2Condition.text = DaysConditions[1]
                        day3Condition.text = DaysConditions[2]
                    }

                    //Formatira datum na skraćenicu imena dana i postavlja na TV
                    runOnUiThread {
                        day1Date.text = getDayAbbreviation(DaysDates[0])
                        day2Date.text = getDayAbbreviation(DaysDates[1])
                        day3Date.text = getDayAbbreviation(DaysDates[2])
                    }
                    //Picaso Image load za svaki dan
                    runOnUiThread { Picasso.get().load("https:${DaysIcons[0]}").into(day1Image) }
                    runOnUiThread { Picasso.get().load("https:${DaysIcons[1]}").into(day2Image) }
                    runOnUiThread { Picasso.get().load("https:${DaysIcons[2]}").into(day3Image) }








                } else {
                    Log.i("ApiError","Api response not succesfful")
                }
            }
        })




}

    private fun getDayAbbreviation(inputString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
        val date = dateFormat.parse(inputString) ?: throw IllegalArgumentException("Invalid date format")

        val dayAbbreviationFormat = SimpleDateFormat("E", Locale("en"))
        return dayAbbreviationFormat.format(date)
    }
}
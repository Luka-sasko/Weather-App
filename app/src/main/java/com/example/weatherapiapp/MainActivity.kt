package com.example.weatherapiapp

import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    Log.i("weatherData",weatherData!!.forecast.forecastday[0].day.toString())


                    //Log.i("apiInfo",responseBody.toString().length.toString())
                } else {
                    Log.i("ApiError","Api response not succesfful")
                }
            }
        })


}
}
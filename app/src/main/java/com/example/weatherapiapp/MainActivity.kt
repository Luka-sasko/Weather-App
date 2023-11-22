package com.example.weatherapiapp

import okhttp3.OkHttpClient
import okhttp3.Request
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private lateinit var recyclerView1: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerViewDayInd: RecyclerView

    private lateinit var weatherAdapter: HourlyWeatherAdapter
    private lateinit var hourlyWeatherList: ArrayList<HourlyWeather>
    private lateinit var dayIndAdapter: HourlyWeatherAdapter
    private lateinit var dayIndWeatherList: ArrayList<HourlyWeather>

    private lateinit var dailyAdapter:DailyWeatherAdapter
    private lateinit var dailyWeatherList: ArrayList<DailyWeather>

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

        val loc = findViewById<EditText>(R.id.TVcurrentName)
        val client = OkHttpClient()

        var city = loc.text
        var apiurl = "https://weatherapi-com.p.rapidapi.com/forecast.json?q=${city}&days=3"

        var request = Request.Builder()
            .url(apiurl)
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
                    val adapterWeather = WeatherAdapter(weatherData!!)
                    runMain(adapterWeather)
                } else {
                    Log.i("ApiError", "Api response not succesfful")
                }
            }
        })
        //Promjena grada novi call
        val button = findViewById<ImageButton>(R.id.imageButton)
        button.setOnClickListener {
            Toast.makeText(this, "Promjena na grad ${city}", Toast.LENGTH_SHORT).show()
            city = loc.text
            apiurl = "https://weatherapi-com.p.rapidapi.com/forecast.json?q=${city}&days=3"
            request = Request.Builder()
                .url(apiurl)
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
                        val adapterWeather = WeatherAdapter(weatherData!!)
                        runMain(adapterWeather)
                    } else {
                        Toast.makeText(baseContext,"Nema podataka za navedeni grad",Toast.LENGTH_SHORT).show()
                        Log.i("ApiError", "Api response not succesfful")
                    }
                }
            })
        }

    }

    private fun runMain(adapterWeather: WeatherAdapter) {

        val region = findViewById<TextView>(R.id.tvRegion)
        val currentTemp = findViewById<TextView>(R.id.TvCurrentTemp)
        val feelTemp = findViewById<TextView>(R.id.tvFeelTemp)
        val currentUv = findViewById<TextView>(R.id.tvCurrentUV)
        val currentPresure = findViewById<TextView>(R.id.tvCurrentPressure)
        val windSpeed = findViewById<TextView>(R.id.tvWindKph)
        val winDir = findViewById<TextView>(R.id.tvWindDir)
        val lastUpdated = findViewById<TextView>(R.id.TvLastUpdated)
        val sunSet = findViewById<TextView>(R.id.tvSunset)
        val sunRise = findViewById<TextView>(R.id.tvSunrise)

        var currentData: MutableList<String> = mutableListOf()
        var weatherHourly: ArrayList<HourlyWeather>
        var dailyWeather:ArrayList<DailyWeather>

        //currentData {Loc,Region,currentTemp,feelTemp,currentUV,currentPresure,windSpeed,windDir,LastUpdated}
        currentData = adapterWeather.GetCurrentData()
        weatherHourly = adapterWeather.generateWeatherHourly()
        dailyWeather = adapterWeather.GenerateDailyWeather()

        runOnUiThread() {SetRecyclerViewDays(dailyWeather,adapterWeather)}

        runOnUiThread() { SetRecyclerViewHourly(weatherHourly) }

        //Postavljanje trenutnih vrijednosti
        runOnUiThread {
            region.text = currentData[1]
            currentTemp.text = currentData[2]
            feelTemp.text = currentData[3]
            currentUv.text = currentData[4]
            currentPresure.text = currentData[5]
            windSpeed.text = currentData[6]
            winDir.text = currentData[7]
            lastUpdated.text = currentData[8]
            sunRise.text = currentData[9]
            sunSet.text = currentData[10]
        }


    }

    private fun SetRecyclerViewDays(dailyWeather: ArrayList<DailyWeather>,adapterWeather: WeatherAdapter) {
        recyclerView2 = findViewById(R.id.daysRecyclerView)
        recyclerView2.setHasFixedSize(true)
        dailyWeatherList=dailyWeather
        dailyAdapter = DailyWeatherAdapter(dailyWeatherList)
        val layoutManager = LinearLayoutManager(this)
        recyclerView2.layoutManager = layoutManager
        recyclerView2.adapter=dailyAdapter
        val tv=findViewById<TextView>(R.id.textView2)
        dailyAdapter.onItemClick = {
            val itemClicked = it.index
            tv.visibility= View.VISIBLE
            tv.text="Forecast for day: ${it.day}"
            val dayIndWeather= adapterWeather.GenerateDayData(itemClicked)
            Toast.makeText(this,"Generated forecast for day:\n${it.day}",Toast.LENGTH_SHORT).show()
            SetRecyclerViewDayInd(dayIndWeather)
        }
    }

    private fun SetRecyclerViewHourly(weatherHourly: ArrayList<HourlyWeather>) {
        recyclerView1 = findViewById(R.id.recyclerView)
        recyclerView1.setHasFixedSize(true)
        hourlyWeatherList = weatherHourly
        weatherAdapter = HourlyWeatherAdapter(hourlyWeatherList)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView1.layoutManager = layoutManager
        recyclerView1.adapter = weatherAdapter
    }

    private  fun SetRecyclerViewDayInd(weatherDayInd: ArrayList<HourlyWeather>){
        recyclerViewDayInd = findViewById(R.id.DayIndRecyclerView)
        recyclerViewDayInd.setHasFixedSize(true)
        dayIndWeatherList=weatherDayInd
        dayIndAdapter=HourlyWeatherAdapter(dayIndWeatherList)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewDayInd.layoutManager=layoutManager
        recyclerViewDayInd.adapter=dayIndAdapter
    }
}
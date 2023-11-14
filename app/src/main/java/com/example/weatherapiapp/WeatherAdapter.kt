package com.example.weatherapiapp

import java.text.SimpleDateFormat
import java.util.Locale

class WeatherAdapter(val weatherData: WeatherData) {

    fun GetDaysIcons():MutableList<String>{
        var list:MutableList<String> = mutableListOf()
        for (i in 0..2){
            list.add(weatherData.forecast.forecastday[i].day.condition.icon)
        }
        return list
    }
    fun GetDaysDate():MutableList<String>{
        var list:MutableList<String> = mutableListOf()
        for (i in 0..2){
            list.add(weatherData.forecast.forecastday[i].date)
        }
        return list
    }
    fun GetConditionText():MutableList<String> {
        var list: MutableList<String> = mutableListOf()
        for (i in 0..2) {
            list.add(weatherData.forecast.forecastday[i].day.condition.text)
        }
        return list
    }
    fun GetMaxTemps():MutableList<String> {
        var list: MutableList<String> = mutableListOf()
        for (i in 0..2) {
            list.add(weatherData.forecast.forecastday[i].day.maxtemp_c.toInt().toString()+"°C")
        }
        return list
    }
    fun GetMinTemps():MutableList<String> {
        var list: MutableList<String> = mutableListOf()
        for (i in 0..2) {
            list.add(weatherData.forecast.forecastday[i].day.mintemp_c.toInt().toString()+"°C")
        }
        return list
    }
    fun GetChanceOfRain():MutableList<String> {
        var list: MutableList<String> = mutableListOf()
        for (i in 0..2) {
            list.add(weatherData.forecast.forecastday[i].day.daily_chance_of_rain.toString()+"%")
        }
        return list
    }
    fun GetCurrentData():MutableList<String>{
        var list: MutableList<String> = mutableListOf()

        list.add(weatherData.location.name)
        list.add(weatherData.location.region)
        list.add(weatherData.current.temp_c.toInt().toString()+" °C")
        list.add("Feels like: "+weatherData.current.feelslike_c.toInt().toString()+" °C")
        list.add("UV: "+weatherData.current.uv.toString())
        list.add(weatherData.current.pressure_mb.toInt().toString()+" Pa")
        list.add(weatherData.current.wind_kph.toString()+" km/h")
        list.add(weatherData.current.wind_dir)
        list.add("Last updated: "+SimpleDateFormat("dd/MM/yyyy HH:mm").format (SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("en")).parse(weatherData.current.last_updated)))
        list.add(weatherData.forecast.forecastday[0].astro.sunrise)
        list.add(weatherData.forecast.forecastday[0].astro.sunset)

        return list
    }

}
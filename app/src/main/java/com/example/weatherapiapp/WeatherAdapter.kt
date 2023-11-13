package com.example.weatherapiapp

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

}
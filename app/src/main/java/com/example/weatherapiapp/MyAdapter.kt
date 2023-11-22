package com.example.weatherapiapp
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//Adapter za 24 satnu prognozu
class HourlyWeatherAdapter(private val hourlyWeatherList: ArrayList<HourlyWeather>) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hourly_weathercast, parent, false)
        return HourlyWeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        val hourlyWeather = hourlyWeatherList[position]
        holder.time.text = hourlyWeather.time
        holder.temperature.text = hourlyWeather.temperature
        holder.pressure.text=hourlyWeather.pressure
        holder.loadWeatherIcon(hourlyWeather.image)
        holder.wind.text=hourlyWeather.wind
    }

    override fun getItemCount(): Int {
        return hourlyWeatherList.size
    }

    class HourlyWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.time)
        val temperature: TextView = itemView.findViewById(R.id.temperature)
        val image:ImageView=itemView.findViewById(R.id.image)
        val pressure:TextView=itemView.findViewById(R.id.pressure)
        val wind:TextView=itemView.findViewById(R.id.wind)
        fun loadWeatherIcon(url: String) {
            Glide.with(itemView.context)
                .load(url)
                .into(image )
        }
    }
}
 //Adapter za 3dana prognozu
 class DailyWeatherAdapter(private val dailyWeatherList: ArrayList<DailyWeather>) : RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherViewHolder>() {
     var onItemClick : ((DailyWeather) -> Unit)? = null
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.day_weathercast, parent, false)
         return DailyWeatherViewHolder(view)
     }

     override fun onBindViewHolder(holder: DailyWeatherViewHolder, position: Int) {
         val dailyWeather = dailyWeatherList[position]
         holder.day.text=dailyWeather.day
         holder.chanceOfRain.text=dailyWeather.chance_of_rain
         holder.condition.text=dailyWeather.condition
         holder.temperature.text=dailyWeather.temp
         holder.loadWeatherIcon(dailyWeather.icon)

         holder.itemView.setOnClickListener {
                val clickedPosition = holder.adapterPosition
                onItemClick?.invoke(dailyWeather)

             }
     }

     override fun getItemCount(): Int {
         return dailyWeatherList.size
     }

     class DailyWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val day: TextView = itemView.findViewById(R.id.tvDay)
         val condition: TextView = itemView.findViewById(R.id.dayCondition)
         val image:ImageView=itemView.findViewById(R.id.DayImageView)
         val temperature:TextView=itemView.findViewById(R.id.dayTemp)
         val chanceOfRain:TextView=itemView.findViewById(R.id.dayChanceOfRain)
         fun loadWeatherIcon(url: String) {
             Glide.with(itemView.context)
                 .load(url)
                 .into(image )
         }
     }
 }


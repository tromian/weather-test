package com.tromian.test.weather.ui.week

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tromian.test.weather.model.pojo.DailyWeather
import com.tromian.test.wether.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

class WeekListAdapter(val itemCallback: (itemId: Int) -> Unit) :
    ListAdapter<DailyWeather, WeekListAdapter.ActorsViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DailyWeather>() {
            override fun areItemsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean =
                oldItem.unixTime == newItem.unixTime

            override fun areContentsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean =
                oldItem == newItem
        }
    }


    inner class ActorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val day: TextView = itemView.findViewById(R.id.tvDay)
        private val maxTemp: TextView = itemView.findViewById(R.id.tvMaxTemp)
        private val minTemp: TextView = itemView.findViewById(R.id.tvMinTemp)
        private val description: TextView = itemView.findViewById(R.id.tvDescription)
        private val image: ImageView = itemView.findViewById(R.id.ivDailyIcon)

        fun bind(dailyWeather: DailyWeather) {
            day.text = dateFormat(dailyWeather.unixTime)
            maxTemp.text = dailyWeather.maxTemp.toInt().toString()
            minTemp.text = dailyWeather.minTemp.toInt().toString()
            description.text = dailyWeather.weather?.description
            Glide.with(itemView)
                .load("https://openweathermap.org/img/wn/${dailyWeather.weather?.icon}.png")
                .into(image)
        }

        fun dateFormat(unixTime: Long): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Instant
                    .ofEpochSecond(unixTime)
                    .atZone(ZoneId.systemDefault())
                    .dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
            } else {
                val date = Date(unixTime * 1000L)
                val sdf = SimpleDateFormat("EE", Locale.getDefault()).format(date)
                return sdf
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActorsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ActorsViewHolder(inflater.inflate(R.layout.daily_weather_preview, parent, false))
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            itemCallback(position)
        }
    }
}
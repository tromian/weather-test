package com.tromian.test.weather.ui.week

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tromian.test.weather.data.DailyWeather
import com.tromian.test.wether.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

class WeekListAdapter(val itemCallback: (itemId: Int) -> Unit) :
    ListAdapter<DailyWeather, WeekListAdapter.ActorsViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DailyWeather>() {
            override fun areItemsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean =
                oldItem.dt == newItem.dt

            override fun areContentsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean =
                oldItem == newItem
        }
    }


    inner class ActorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val day: TextView = itemView.findViewById(R.id.tvDay)
        private val maxTemp: TextView = itemView.findViewById(R.id.tvMaxTemp)
        private val minTemp: TextView = itemView.findViewById(R.id.tvMinTemp)

        fun bind(dailyWeather: DailyWeather) {

            day.text = dateFormat(dailyWeather.dt)
            maxTemp.text = dailyWeather.temp.max.toString()
            minTemp.text = dailyWeather.temp.min.toString()
        }

        fun dateFormat(unixTime: Long): String {

            return Instant
                .ofEpochSecond(unixTime)
                .atZone(ZoneId.systemDefault())
                .dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActorsViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ActorsViewHolder(inflater.inflate(R.layout.daily_weather_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            itemCallback(position)
        }
    }
}
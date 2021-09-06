package com.tromian.test.weather.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tromian.test.weather.data.daily.DailyWeather
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentDailyDetailsBinding
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class DetailsFragment : Fragment(R.layout.fragment_daily_details) {

    private var _binding: FragmentDailyDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weather: DailyWeather

    private val detailsViewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDailyDetailsBinding.inflate(inflater, container, false)
        val safeArgs: DetailsFragmentArgs by navArgs()
        weather = safeArgs.dailyWeather
        bindViews(weather)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun bindViews(dailyWeather: DailyWeather) {

        val day = binding.tvDayOfWeek
        val dayN = binding.tvDayNum

        val tempMorning = binding.tvMorning
        val tempDay = binding.tvDay
        val tempEvening = binding.tvEvening
        val tempNight = binding.tvNight

        val clouds = binding.tvClouds
        val weatherImage = binding.ivWeatherIcon
        val pressure = binding.tvPressure
        val description = binding.tvDescription

        day.text = dateFormatToDayOfWeek(dailyWeather.dt)
        dayN.text = dateFormat(dailyWeather.dt)

        tempMorning.text = "${dailyWeather.temp.morn.toInt()} °С"
        tempDay.text = "${dailyWeather.temp.day.toInt()} °С"
        tempEvening.text = "${dailyWeather.temp.eve.toInt()} °С"
        tempNight.text = "${dailyWeather.temp.night.toInt()} °С"

        clouds.text = "${dailyWeather.clouds} %"
        pressure.text = "${dailyWeather.pressure} гПа"
        description.text = dailyWeather.weather[0].description

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${dailyWeather.weather[0].icon}.png")
            .into(weatherImage)


    }

    fun dateFormat(unixTime: Long): String {
        return DateTimeFormatter.ISO_INSTANT
            .format(Instant.ofEpochSecond(unixTime))
            .substringBefore('T')
    }

    fun dateFormatToDayOfWeek(unixTime: Long): String {
        return Instant
            .ofEpochSecond(unixTime)
            .atZone(ZoneId.systemDefault())
            .dayOfWeek.getDisplayName(TextStyle.FULL, Locale.forLanguageTag("ru"))
    }

}
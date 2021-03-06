package com.tromian.test.weather.ui.details

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tromian.test.weather.model.pojo.DailyWeather
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentDailyDetailsBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class DetailsFragment : Fragment(R.layout.fragment_daily_details) {

    private var _binding: FragmentDailyDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var weather: DailyWeather

    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDailyDetailsBinding.bind(view)
        val safeArgs: DetailsFragmentArgs by navArgs()
        weather = safeArgs.dailyWeather
        bindViews(weather)
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
        val backButton = binding.ivBack

        backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        day.text = dateFormatToDayOfWeek(dailyWeather.unixTime)
        dayN.text = dateFormat(dailyWeather.unixTime)

        tempMorning.text = "${dailyWeather.mornTemp.toInt()} ????"
        tempDay.text = "${dailyWeather.dayTemp.toInt()} ????"
        tempEvening.text = "${dailyWeather.eveTemp.toInt()} ????"
        tempNight.text = "${dailyWeather.nightTemp.toInt()} ????"

        clouds.text = "${dailyWeather.clouds} %"
        pressure.text = "${dailyWeather.pressure} ??????"
        description.text = dailyWeather.weather?.description



        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${dailyWeather.weather?.icon}.png")
            .into(weatherImage)
    }

    private fun dateFormat(unixTime: Long): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ISO_INSTANT
                .format(Instant.ofEpochSecond(unixTime))
                .substringBefore('T')
        } else {
            val date = Date(unixTime * 1000L)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
            return sdf
        }
    }

    private fun dateFormatToDayOfWeek(unixTime: Long): String {
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
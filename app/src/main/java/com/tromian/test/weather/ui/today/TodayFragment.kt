package com.tromian.test.weather.ui.today

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.tromian.test.weather.appComponent
import com.tromian.test.weather.data.CityWeather
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.ui.MainActivity
import com.tromian.test.weather.ui.ViewModelsFactory
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentTodayBinding
import javax.inject.Inject

class TodayFragment : Fragment(R.layout.fragment_today) {

    @Inject
    lateinit var repository: WeatherRepository

    private val todayViewModel by viewModels<TodayViewModel> {
        ViewModelsFactory(repository)
    }
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Log.i(TAG, "$this")

        setupDataObservers()
        return root
    }


    private fun setupDataObservers() {
        (activity as MainActivity).autocompletePlaceResult.observe(viewLifecycleOwner, {
            todayViewModel.defaultCity = it.name.toString()
            todayViewModel.loadWeather()
        })

        todayViewModel.cityWeather.observe(viewLifecycleOwner, Observer {
            bindViews(it)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @SuppressLint("SetTextI18n")
    private fun bindViews(cityWeather: CityWeather) {
        val temp = binding.tvCurrentTemperature
        val feelTemp = binding.tvFeelingTemperature
        val clouds = binding.tvClouds
        val weatherImage = binding.ivWeatherIcon
        val pressure = binding.tvPressure

        temp.text = "${cityWeather.main.temp}"
        feelTemp.text = "${cityWeather.main.feelsLike}"
        clouds.text = "${cityWeather.clouds.all} %"
        pressure.text = "${cityWeather.main.pressure} гПа"
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${cityWeather.weather[0].icon}.png")
            .into(weatherImage)

    }


    companion object {
        const val TAG = "today"
    }

}
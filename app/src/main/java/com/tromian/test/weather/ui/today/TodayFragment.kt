package com.tromian.test.weather.ui.today

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tromian.test.weather.appComponent
import com.tromian.test.weather.model.ErrorResult
import com.tromian.test.weather.model.PendingResult
import com.tromian.test.weather.model.SuccessResult
import com.tromian.test.weather.model.WeatherRepository
import com.tromian.test.weather.model.pojo.CurrentWeather
import com.tromian.test.weather.ui.ViewModelsFactory
import com.tromian.test.weather.ui.activityViewModel
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentTodayBinding
import javax.inject.Inject

class TodayFragment : Fragment(R.layout.fragment_today) {

    @Inject
    lateinit var repository: WeatherRepository

    private val viewModel by viewModels<TodayViewModel> {
        ViewModelsFactory(repository)
    }
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTodayBinding.bind(view)
        setupDataObservers()
    }

    private fun setupDataObservers() {
        activityViewModel().place.observe(viewLifecycleOwner, {
            viewModel.loadWeather(it)
        })
        viewModel.cityWeatherResult.observe(viewLifecycleOwner, {
            when (it) {
                is PendingResult -> showPending()
                is ErrorResult -> showError()
                is SuccessResult -> showSuccess(it.data)
            }
        })
    }

    private fun showSuccess(data: CurrentWeather) {
        bindViews(data)
    }

    private fun showError() {
        binding.todayData.root.children.forEach {
            if (it.id == R.id.error_layout) {
                binding.todayData.errorLayout.root.visibility = View.VISIBLE
                binding.todayData.errorLayout.btnRetry.setOnClickListener {
                    val place = activityViewModel().place.value ?: return@setOnClickListener
                    viewModel.loadWeather(place)
                }
            } else it.visibility = View.GONE
        }
    }

    private fun showPending() {
        binding.todayData.root.children.forEach {
            if (it.id == R.id.progressBarToday) {
                it.visibility = View.VISIBLE
            } else it.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun bindViews(currentWeather: CurrentWeather) {
        binding.todayData.root.children.forEach {
            if (it.id == R.id.error_layout || it.id == R.id.progressBarToday) {
                it.visibility = View.GONE
            } else it.visibility = View.VISIBLE
        }
        val temp = binding.todayData.tvCurrentTemperature
        val feelTemp = binding.todayData.tvFeelingTemperature
        val clouds = binding.todayData.tvClouds
        val weatherImage = binding.todayData.ivWeatherIcon
        val pressure = binding.todayData.tvPressure
        val description = binding.todayData.tvDescription

        temp.text = "${currentWeather.currentTemp.toInt()} ????"
        feelTemp.text = "${currentWeather.feelsLike.toInt()} ????"
        clouds.text = "${currentWeather.clouds} %"
        pressure.text = "${currentWeather.pressure} ??????"

        description.text = currentWeather.weather?.description

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${currentWeather.weather?.icon}.png")
            .into(weatherImage)


    }

}
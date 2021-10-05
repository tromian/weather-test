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
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.current.CurrentWeather
import com.tromian.test.weather.model.ErrorResult
import com.tromian.test.weather.model.PendingResult
import com.tromian.test.weather.model.SuccessResult
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
        binding.root.children.forEach {
            if (it.id == R.id.error_layout) {
                binding.errorLayout.root.visibility = View.VISIBLE
                binding.errorLayout.btnRetry.setOnClickListener {
                    val place = activityViewModel().place.value ?: return@setOnClickListener
                    viewModel.loadWeather(place)
                }
            } else it.visibility = View.GONE
        }
    }

    private fun showPending() {
        binding.root.children.forEach {
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
        binding.root.children.forEach {
            if (it.id == R.id.error_layout || it.id == R.id.progressBarToday) {
                binding.errorLayout.root.visibility = View.GONE
                it.visibility = View.GONE
            } else it.visibility = View.VISIBLE
        }
        val temp = binding.tvCurrentTemperature
        val feelTemp = binding.tvFeelingTemperature
        val clouds = binding.tvClouds
        val weatherImage = binding.ivWeatherIcon
        val pressure = binding.tvPressure
        val description = binding.tvDescription

        temp.text = "${currentWeather.main.temp.toInt()} °С"
        feelTemp.text = "${currentWeather.main.feelsLike.toInt()} °С"
        clouds.text = "${currentWeather.clouds.all} %"
        pressure.text = "${currentWeather.main.pressure} гПа"
        description.text = currentWeather.weather[0].description

        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${currentWeather.weather[0].icon}.png")
            .into(weatherImage)


    }


    companion object {
        const val TAG = "today"
    }

}
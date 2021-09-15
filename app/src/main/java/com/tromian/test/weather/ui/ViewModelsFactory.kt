package com.tromian.test.weather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.ui.main.MainViewModel
import com.tromian.test.weather.ui.map.MapViewModel
import com.tromian.test.weather.ui.splash.SplashViewModel
import com.tromian.test.weather.ui.today.TodayViewModel
import com.tromian.test.weather.ui.week.WeekViewModel

class ViewModelsFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(repository)
            }
            TodayViewModel::class.java -> {
                TodayViewModel(repository)
            }
            WeekViewModel::class.java -> {
                WeekViewModel(repository)
            }
            SplashViewModel::class.java -> {
                SplashViewModel(repository)
            }
            MapViewModel::class.java -> {
                MapViewModel()
            }
            else -> throw IllegalStateException("Unknown view model class")
        }
        return viewModel as T
    }
}
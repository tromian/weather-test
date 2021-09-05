package com.tromian.test.weather.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tromian.test.weather.data.CityWeather
import com.tromian.test.weather.data.WeatherRepository
import kotlinx.coroutines.launch

class TodayViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _cityWeather = MutableLiveData<CityWeather?>()
    val cityWeather: LiveData<CityWeather?> = _cityWeather
    var defaultCity = "Kyiv"

    init {
        loadWeather(defaultCity)
    }

    fun loadWeather(city: String) {
        viewModelScope.launch {
            _cityWeather.postValue(repository.loadTodayByCityName(city))
        }
    }
}
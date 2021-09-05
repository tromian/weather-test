package com.tromian.test.weather.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.current.CurrentWeather
import kotlinx.coroutines.launch

class TodayViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _cityWeather = MutableLiveData<CurrentWeather?>()
    val cityWeather: LiveData<CurrentWeather?> = _cityWeather


    fun loadWeather(place: Place) {
        viewModelScope.launch {
            _cityWeather.postValue(repository.loadCurrentWeatherByCoord(place))
        }
    }
}
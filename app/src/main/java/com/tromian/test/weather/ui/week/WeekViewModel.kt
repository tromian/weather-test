package com.tromian.test.weather.ui.week

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.DailyWeather
import com.tromian.test.weather.data.WeatherRepository
import kotlinx.coroutines.launch

class WeekViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _dailyList = MutableLiveData<List<DailyWeather>>()
    val dailyWeatherList: LiveData<List<DailyWeather>> = _dailyList

    fun loadWeeklyWeather(place: Place) {
        viewModelScope.launch {
            val remoteList = repository.loadWeeklyWeatherList(place)
            _dailyList.postValue(remoteList)
        }
    }

}
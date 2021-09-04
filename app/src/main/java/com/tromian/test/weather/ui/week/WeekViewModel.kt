package com.tromian.test.weather.ui.week

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.daily.Daily
import kotlinx.coroutines.launch

class WeekViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _dailyList = MutableLiveData<List<Daily>>()
    val dailyList: LiveData<List<Daily>> = _dailyList

    fun loadWeeklyWeather(place: Place) {
        viewModelScope.launch {
            val remoteList = repository.loadWeeklyWeatherList(place)
            _dailyList.postValue(remoteList)
        }
    }

}
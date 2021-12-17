package com.tromian.test.weather.ui.week

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.model.ErrorResult
import com.tromian.test.weather.model.PendingResult
import com.tromian.test.weather.model.SuccessResult
import com.tromian.test.weather.model.WeatherRepository
import com.tromian.test.weather.model.pojo.DailyWeather
import com.tromian.test.weather.ui.LiveResult
import com.tromian.test.weather.ui.MutableLiveResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeekViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _dailyList = MutableLiveResult<List<DailyWeather>>()
    val dailyWeatherList: LiveResult<List<DailyWeather>> = _dailyList

    fun loadWeeklyWeather(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            _dailyList.postValue(PendingResult())

            loadLocalData()

            loadRemoteData(place)

        }
    }

    private fun loadLocalData() {
        val localData = repository.getDailyWeatherListFromDB()
        if (localData.isNotEmpty()) {
            _dailyList.postValue(SuccessResult(localData))
        }
    }

    private suspend fun loadRemoteData(place: Place) {
        val remoteData = repository.loadWeeklyWeatherList(place)
        if (remoteData.isEmpty()) {
            _dailyList.postValue(ErrorResult(IllegalStateException("no data")))
        } else {
            repository.saveDailyWeatherListToDB(remoteData)
            loadLocalData()
        }
    }

}
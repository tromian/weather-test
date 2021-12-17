package com.tromian.test.weather.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.model.ErrorResult
import com.tromian.test.weather.model.PendingResult
import com.tromian.test.weather.model.SuccessResult
import com.tromian.test.weather.model.WeatherRepository
import com.tromian.test.weather.model.pojo.CurrentWeather
import com.tromian.test.weather.ui.LiveResult
import com.tromian.test.weather.ui.MutableLiveResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodayViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _cityWeatherResult = MutableLiveResult<CurrentWeather>()
    val cityWeatherResult: LiveResult<CurrentWeather> = _cityWeatherResult

    fun loadWeather(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {

            _cityWeatherResult.postValue(PendingResult())

            loadLocalData(place.name!!)

            loadRemoteData(place)

        }
    }

    private fun loadLocalData(city: String) {
        val localData = repository.getCurrentWeatherFromDB(city)
        if (localData != null) {
            _cityWeatherResult.postValue(SuccessResult(localData))
        }
    }

    private suspend fun loadRemoteData(place: Place) {
        val remoteData = repository.loadCurrentWeatherByCoord(place)
        if (remoteData == null) {
            _cityWeatherResult.postValue(ErrorResult(IllegalStateException("no data")))
        } else {
            repository.saveCurrentWeatherToDB(remoteData)
            loadLocalData(place.name!!)
        }
    }
}
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
import timber.log.Timber

class TodayViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _cityWeatherResult = MutableLiveResult<CurrentWeather>()
    val cityWeatherResult: LiveResult<CurrentWeather> = _cityWeatherResult

    fun loadWeather(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {

            _cityWeatherResult.postValue(PendingResult())

            loadData(place)

        }
    }

    private suspend fun loadData(place: Place) {
        val localData = repository.getCurrentWeatherFromDB(place.name!!)
        Timber.tag("data").d("localData:  $localData")
        if (localData != null) {
            _cityWeatherResult.postValue(SuccessResult(localData))
        }
        val remoteData = repository.loadCurrentWeatherByCoord(place)
        Timber.tag("data").d("remoteData:  $remoteData")
        if (localData == null && remoteData == null) {
            _cityWeatherResult.postValue(ErrorResult(IllegalStateException("no data")))
        } else if (remoteData != null) {
            repository.saveCurrentWeatherToDB(remoteData)
            _cityWeatherResult.postValue(SuccessResult(remoteData))
        }

    }

}
package com.tromian.test.weather.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.current.CurrentWeather
import com.tromian.test.weather.model.ErrorResult
import com.tromian.test.weather.model.PendingResult
import com.tromian.test.weather.model.SuccessResult
import com.tromian.test.weather.ui.LiveResult
import com.tromian.test.weather.ui.MutableLiveResult
import kotlinx.coroutines.launch

class TodayViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _cityWeatherResult = MutableLiveResult<CurrentWeather>()
    val cityWeatherResult: LiveResult<CurrentWeather> = _cityWeatherResult

    fun loadWeather(place: Place) {
        viewModelScope.launch {
            _cityWeatherResult.postValue(PendingResult())
            val remoteData = repository.loadCurrentWeatherByCoord(place)
            if (remoteData == null) {
                _cityWeatherResult.postValue(ErrorResult(IllegalStateException("no data")))
            } else {
                _cityWeatherResult.postValue(SuccessResult(remoteData))
            }

        }
    }
}
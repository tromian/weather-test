package com.tromian.test.weather.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tromian.test.weather.data.toPlace
import com.tromian.test.weather.model.WeatherRepository
import com.tromian.test.weather.model.pojo.CurrentCity
import com.tromian.test.weather.utils.AppConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel(
    private val repository: WeatherRepository
) : ViewModel() {


    val defaultLocation = CurrentCity(
        AppConstants.KYIV_NAME,
        AppConstants.KYIV_LAT,
        AppConstants.KYIV_LON
    )

    fun loadLocalDataIfExist() = viewModelScope.launch(Dispatchers.IO) {
        val location = checkLastLocation()
        Timber.tag("data").d("Initial Location:  $location")
        if (repository.checkIfNeedUpdate(location.cityName)) {
            updateWeatherData(location)
        }
    }

    private fun checkLastLocation(): CurrentCity {
        val location = repository.getLocationFromDB()
        if (location == null) {
            repository.saveLocationToDB(defaultLocation)
            return defaultLocation
        }
        return location
    }

    private suspend fun updateWeatherData(location: CurrentCity) {
        val newCurrentData = repository.loadCurrentWeatherByCoord(location.toPlace()) ?: return
        repository.saveCurrentWeatherToDB(newCurrentData)
        Timber.tag("data").d("newCurrentData:  $newCurrentData")

        val newDailyData = repository.loadWeeklyWeatherList(location.toPlace())
        if (newDailyData.isEmpty()) return
        Timber.tag("data").d("newDailyData:  $newDailyData")
        repository.saveDailyWeatherListToDB(newDailyData)
    }

}
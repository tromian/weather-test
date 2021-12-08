package com.tromian.test.weather.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tromian.test.weather.AppConstants
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.toPlace
import com.tromian.test.weather.model.pojo.CurrentCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val newDailyData = repository.loadWeeklyWeatherList(location.toPlace())
        if (newDailyData.isEmpty()) return
        repository.saveDailyWeatherListToDB(newDailyData)
    }

}
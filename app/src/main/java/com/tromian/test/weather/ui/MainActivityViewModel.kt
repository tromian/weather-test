package com.tromian.test.weather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.data.WeatherRepository
import com.tromian.test.weather.data.toCity
import com.tromian.test.weather.data.toPlace
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _place = MutableLiveData<Place>().apply {
        value = repository.getLocationFromDB()?.toPlace()
    }
    val place: LiveData<Place> = _place

    fun updatePlace(place: Place) {
        viewModelScope.launch {
            repository.saveLocationToDB(place.toCity())
            _place.postValue(repository.getLocationFromDB()?.toPlace())
        }
    }

}
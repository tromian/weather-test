package com.tromian.test.weather.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.AppConstants
import com.tromian.test.weather.data.WeatherRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _place = MutableLiveData<Place>().apply {
        value = Place.builder()
            .setLatLng(LatLng(AppConstants.KYIV_LAT, AppConstants.KYIV_LON))
            .setName(AppConstants.KYIV_NAME)
            .build()
    }
    val place: LiveData<Place> = _place

    fun updatePlace(place: Place) {
        viewModelScope.launch {
            _place.postValue(place)
        }
    }

}
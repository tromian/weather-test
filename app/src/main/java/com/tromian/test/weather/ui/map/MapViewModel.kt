package com.tromian.test.weather.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel : ViewModel() {

    private val _coordinate = MutableLiveData<LatLng>().apply {
    }
    val coordinate: LiveData<LatLng> = _coordinate

    fun setCoordinate(coord: LatLng) {
        _coordinate.postValue(coord)
    }

}
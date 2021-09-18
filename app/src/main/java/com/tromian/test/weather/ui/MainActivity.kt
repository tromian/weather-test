package com.tromian.test.weather.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.AppConstants
import com.tromian.test.wether.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val hostPlace = MutableLiveData<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initPlaces()
    }

    private fun initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, AppConstants.GOOGLE_API_KEY)
            Places.createClient(this)
        }
    }

    fun updatePlace(place: Place) {
        lifecycleScope.launch {
            hostPlace.postValue(place)
        }
    }

}
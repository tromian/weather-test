package com.tromian.test.weather.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.Places
import com.tromian.test.weather.appComponent
import com.tromian.test.weather.model.WeatherRepository
import com.tromian.test.weather.utils.AppConstants
import com.tromian.test.wether.databinding.ActivityMainBinding
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var repository: WeatherRepository
    val viewModel by viewModels<MainActivityViewModel> {
        ViewModelsFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationContext.appComponent.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initPlaces()
    }

    private fun initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, AppConstants.GOOGLE_API_KEY, Locale.getDefault())
            Places.createClient(this)
        }
    }

}
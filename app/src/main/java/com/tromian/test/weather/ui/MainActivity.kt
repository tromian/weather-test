package com.tromian.test.weather.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tromian.test.weather.AppConstants
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val AUTOCOMPLETE_REQUEST_CODE = 1
    private lateinit var toolbar: Toolbar
    val autocompletePlaceResult = MutableLiveData<Place>().apply {
        value = Place.builder()
            .setLatLng(LatLng(AppConstants.KYIV_LAT, AppConstants.KYIV_LON))
            .setName(AppConstants.KYIV_NAME)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setupToolbar()
        val navView: BottomNavigationView = binding.navView
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, AppConstants.MAP_KEY)
        }
        Places.createClient(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navView.setupWithNavController(navController)
    }

    fun updatePlace(latLng: LatLng) {

    }

    private fun autoCompleteRun() {
        val fields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .setTypeFilter(TypeFilter.CITIES)
            .build(this)
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.today_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.search_item) {
            autoCompleteRun()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        toolbar = binding.toolbar
        toolbar.title = autocompletePlaceResult.value?.name
        autocompletePlaceResult.observe(this, {
            toolbar.title = it.name
        })
        setSupportActionBar(toolbar)
        toolbar.inflateMenu(R.menu.today_toolbar_menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        autocompletePlaceResult.postValue(place)
                        Log.i("MainActivity", "Place: ${place.name}, ${place.latLng}")
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Log.i("MainActivity", status.statusMessage)
                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
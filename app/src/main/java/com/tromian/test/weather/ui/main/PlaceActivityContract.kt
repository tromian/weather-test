package com.tromian.test.weather.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

class PlaceActivityContract : ActivityResultContract<String, Place?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        val fields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
        return Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .setTypeFilter(TypeFilter.CITIES)
            .build(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Place? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> Autocomplete.getPlaceFromIntent(intent!!)
    }


}
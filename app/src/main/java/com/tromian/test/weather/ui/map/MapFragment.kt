package com.tromian.test.weather.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.ui.activityViewModel
import com.tromian.test.weather.utils.AppConstants.REQUEST_CODE_LOCATION_PERMISSION
import com.tromian.test.weather.utils.TrackingUtility
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentMapBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber
import java.io.IOException
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map),
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener,
    EasyPermissions.PermissionCallbacks {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationManager: LocationManager

    private val viewModel: MapViewModel by viewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private var marker: Marker? = null
    private lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)
        mapView = binding.mapView
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setupDataObservers()
        setCurrentLocationChecker()
    }

    private fun setCurrentLocationChecker() {
        binding.floatingMyLocation.setOnClickListener {
            requestPermissions()
        }
    }

    private fun setupDataObservers() {
        activityViewModel().place.observe(viewLifecycleOwner, {
            it.latLng?.let { latlng -> viewModel.setCoordinate(latlng) }
        })

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        _binding = null
        mFusedLocationClient = null
        super.onDestroyView()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isMyLocationButtonEnabled = true
        viewModel.coordinate.observe(viewLifecycleOwner, {
            updateMarkerLocation(it)
        })
        map.setOnMarkerClickListener(this)
        map.setOnMapClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        setNewPlace(marker)
        return false
    }

    override fun onMapClick(latLng: LatLng) {
        updateMarkerLocation(latLng)
    }

    private fun updateMarkerLocation(latLng: LatLng) {
        map.clear()
        marker = map.addMarker(
            MarkerOptions()
                .position(latLng)
        )
        if (marker != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker!!.position, 12f))
        }
        marker?.tag = MARKER_TAG
    }

    private fun setNewPlace(marker: Marker) {
        if (marker.tag == MARKER_TAG) {
            try {
                val geocode = Geocoder(requireContext(), Locale.getDefault())
                    .getFromLocation(marker.position.latitude, marker.position.longitude, 1)
                val address = geocode[0].locality
                if (address != null) {
                    val newPlace = Place.builder()
                        .setName(address)
                        .setLatLng(marker.position).build()
                    newPlace.latLng?.let { viewModel.setCoordinate(it) }
                    activityViewModel().updatePlace(newPlace)
                    findNavController().navigate(R.id.navigation_today)
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
                return
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

        }
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {

            setCurrentLocation()
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    }

    @SuppressLint("MissingPermission")
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        setCurrentLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }


    @SuppressLint("MissingPermission")
    private fun setCurrentLocation() {
        lifecycleScope.launchWhenResumed {

            mFusedLocationClient?.lastLocation?.addOnCompleteListener(requireActivity()) { task ->
                val location: Location? = task.result

                if (location != null) {
                    Timber.d("lat : " + location.latitude + " lon : " + location.longitude + "  ")
                    updateMarkerLocation(LatLng(location.latitude, location.longitude))
                }
            }

        }

    }

    companion object {
        const val MARKER_TAG = "marker"
    }
}

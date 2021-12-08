package com.tromian.test.weather.ui.map

import android.Manifest
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.tromian.test.weather.AppConstants.REQUEST_CODE_LOCATION_PERMISSION
import com.tromian.test.weather.TrackingUtility
import com.tromian.test.weather.ui.activityViewModel
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentMapBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener,
    EasyPermissions.PermissionCallbacks {

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

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setupDataObservers()
    }

    private fun setupDataObservers() {
        activityViewModel().place.observe(viewLifecycleOwner, {
            it.latLng?.let { latlng -> viewModel.setCoordinate(latlng) }
        })

    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
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

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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

    override fun onMarkerClick(marker: Marker): Boolean {
        setNewPlace(marker)
        return false
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

    override fun onMapClick(latLng: LatLng) {
        updateMarkerLocation(latLng)
    }

    companion object {
        const val MARKER_TAG = "marker"
    }

}
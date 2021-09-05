package com.tromian.test.weather.ui.map

import android.Manifest
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.tromian.test.weather.ui.MainActivity
import com.tromian.test.wether.R
import com.tromian.test.wether.databinding.FragmentMapBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener,
    EasyPermissions.PermissionCallbacks {

    private val mapViewModel: MapViewModel by viewModels()
    private var _binding: FragmentMapBinding? = null

    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private var marker: Marker? = null
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
//        binding.floatingMyLocation.setOnClickListener {
//            requestPermissions()
//        }
        setupDataObservers()
        return root
    }

    private fun setupDataObservers() {
        (activity as MainActivity).autocompletePlaceResult.observe(viewLifecycleOwner, {
            it.latLng?.let { latlng -> mapViewModel.setCoordinate(latlng) }
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
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        map.uiSettings.isMyLocationButtonEnabled = true
        mapViewModel.coordinate.observe(viewLifecycleOwner, Observer {
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
            val geocode = Geocoder(requireContext(), Locale.ENGLISH)
                .getFromLocation(marker.position.latitude, marker.position.longitude, 1)
            val address = geocode[0].locality
            if (address != null) {
                val newPlace = Place.builder()
                    .setName(address)
                    .setLatLng(marker.position).build()
                (activity as MainActivity).updatePlace(newPlace)
                findNavController().navigate(R.id.navigation_today)
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
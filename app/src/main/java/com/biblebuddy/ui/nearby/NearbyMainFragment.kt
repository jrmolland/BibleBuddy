package com.biblebuddy.ui.nearby

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.biblebuddy.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted


class NearbyMainFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var nearbyViewModel: NearbyViewModel
    private lateinit var btnLocation: ImageButton
//    private lateinit var searchView: SearchView

    private lateinit var locationPermission: String

    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location

    private lateinit var client: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        nearbyViewModel =
            ViewModelProvider(this).get(NearbyViewModel::class.java)
        locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                currentLocation = locationResult.locations[0]
            }
        }

        return inflater.inflate(R.layout.fragment_nearby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        client = LocationServices.getFusedLocationProviderClient(activity)

        btnLocation = view.findViewById(R.id.btn_get_location)
//        searchView = view.findViewById(R.id.search_bar)

        btnLocation.setOnClickListener {
            accessLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        googleMap?.let {
            map = it
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = false
            accessLocation()
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(123)
    private fun accessLocation() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                locationPermission
            )
        ) {
            var locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var locationServiceEnabled =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
                )

            if (locationServiceEnabled) {
                client.lastLocation.addOnCompleteListener {
                    var location = it.result

                    if (location != null) {
                        Toast.makeText(requireContext(), "Please wait...", Toast.LENGTH_SHORT)
                            .show()

                        updateCurrentLocation(location.latitude, location.longitude)
                    } else {
                        var locationRequest = LocationRequest.create().apply {
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                            interval = 5000
                            fastestInterval = 1000
                        }

                        client.requestLocationUpdates(locationRequest, locationCallback, null)
                    }
                }
            }

        } else {
            EasyPermissions.requestPermissions(
                this,
                "Need to find your nearest group!",
                123,
                locationPermission
            )
        }
    }

    private fun updateCurrentLocation(lat: Double, long: Double) {
        val sydney = LatLng(lat, long)
//        map.addMarker(MarkerOptions().position(sydney).title("John's Dorm Room"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
    }
}
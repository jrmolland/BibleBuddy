package com.biblebuddy.ui.nearby

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.Loader
import com.biblebuddy.R
import com.biblebuddy.SharedViewModel
import com.biblebuddy.data.model.GroupLocation
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted


class NearbyMainFragment : Fragment() {

    private lateinit var map: GoogleMap
    private var mapReady = false

    private lateinit var btnLocation: ImageButton
//    private lateinit var searchView: SearchView

    private lateinit var model: SharedViewModel

    private lateinit var locationPermission: String

    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location

    private lateinit var client: FusedLocationProviderClient

    private var locations = mutableListOf<GroupLocation>()

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        var rootView = inflater.inflate(R.layout.fragment_nearby, container, false)

        locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                currentLocation = locationResult.locations[0]
            }
        }

        accessLocation()

        model = ViewModelProvider(this).get(SharedViewModel::class.java)
        client = LocationServices.getFusedLocationProviderClient(activity)

        btnLocation = rootView.findViewById(R.id.btn_get_location)
        btnLocation.setOnClickListener {
            findCurrentLocation()
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync { googleMap ->
            map = googleMap
            mapReady = true
            model.fetchGroups()
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        37.58786390490888,
                        -78.77005423203961
                    ), 5f
                )
            )
        }

        model.groups.observe(viewLifecycleOwner, Observer { groups ->
            locations = groups
            drawMarkers()
        })

        return rootView
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

            if (locationServiceEnabled && mapReady) {
                // Need to have access first
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = false
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 7f))
    }

    @SuppressLint("MissingPermission")
    private fun findCurrentLocation() {
        client.lastLocation.addOnCompleteListener {
            var location = it.result

//            val args: NearbyMainFragmentArgs by navArgs()
//            if(args.groupID > -1) {
//                var latLng = model.getGroupAtIndex(args.groupID).latLong
//                updateCurrentLocation(latLng.latitude,latLng.longitude)
//                Toast.makeText(requireContext(), "$latLng.latitude}", Toast.LENGTH_SHORT)
//                    .show()
//            }

            if (location != null) {
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

    private fun drawMarkers() {
        if (locations != null && mapReady) {
            locations.forEach { group ->
                var marker = MarkerOptions().position(group.latLong).title(group.description)
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.holy_bible))
                map.addMarker(marker)
            }
        }
    }
}
package com.biblebuddy.data.model

import com.google.android.gms.maps.model.LatLng

data class GroupLocation(
    val latLong: LatLng,
    val phone: String,
    val host: String,
    val description: String
)

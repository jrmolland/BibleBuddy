package com.biblebuddy.data.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.QueryDocumentSnapshot

class GroupLocation {
    lateinit var latLong: LatLng
    lateinit var phone: String
    lateinit var host: String
    lateinit var description: String

    constructor(
        latLong: LatLng,
        phone: String,
        host: String,
        description: String
    ) {
        this.latLong = latLong
        this.phone = phone
        this.host = host
        this.description = description
    }

    constructor(document: QueryDocumentSnapshot) {
        var host = document.data.getValue("host") as String
        var phone = document.data.getValue("phone") as String
        var description = document.data.getValue("description") as String
        var point = document.getGeoPoint("location")

        this.latLong = LatLng(point!!.latitude, point!!.longitude)
        this.phone = phone
        this.host = host
        this.description = description
    }
}


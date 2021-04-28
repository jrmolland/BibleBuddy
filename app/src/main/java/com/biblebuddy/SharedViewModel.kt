package com.biblebuddy

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.biblebuddy.data.model.GroupLocation
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlin.random.Random

class SharedViewModel : ViewModel() {
    var groups: MutableLiveData<ArrayList<GroupLocation>> = MutableLiveData()

    fun fetchGroups() {
//        Location.distanceBetween()

        var db = FirebaseFirestore.getInstance()
        db.collection("bible-studies")
            .get()
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    groups.postValue(ArrayList(it.result.map {
                        GroupLocation(it)
                    }))
                } else {
                    groups.postValue(null)
                }
            }

    }

    fun populateData() {
        var db = FirebaseFirestore.getInstance()

        var descriptions = listOf("Lorem Ipsum Description", "Description Lorem Ipsum")
        var phones = listOf("9999999999", "1111111111", "5555555555")
        var names = listOf("John", "Samuel", "Rasputin")
        var latitudes = List(100) {
            Random.nextDouble(25.0, 50.0)
        }

        var longitudes = List(100) {
            Random.nextDouble(-160.0, -69.0)
        }

        for (i in 1..200) {
            val data = hashMapOf(
                "description" to descriptions.random(),
                "phone" to phones.random(),
                "host" to names.random(),
                "location" to GeoPoint(latitudes.random(), longitudes.random())
            )
            db.collection("bible-studies").add(data)

        }
    }
}
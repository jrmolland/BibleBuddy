package com.biblebuddy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.biblebuddy.data.model.GroupLocation
import com.google.firebase.firestore.FirebaseFirestore

class SharedViewModel : ViewModel() {
    var groups: MutableLiveData<ArrayList<GroupLocation>> = MutableLiveData()

//    fun getDataListObserver(): MutableLiveData<List<GroupLocation>> {
//        return groups
//    }

    fun fetchGroups() {
        var db = FirebaseFirestore.getInstance()
        db.collection("bible-studies")
            .get()
            .addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    Log.d("TEST", "Updating Data...")
                    groups.postValue(ArrayList(it.result.map {
                        GroupLocation(it)
                    }))
                } else {
                    groups.postValue(null)
                }
            }
    }
}
package com.biblebuddy.ui.nearby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.biblebuddy.R
import com.google.android.gms.maps.SupportMapFragment

class NearbyFragment : Fragment() {

    private lateinit var nearbyViewModel: NearbyViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        nearbyViewModel =
                ViewModelProvider(this).get(NearbyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_nearby, container, false)

        return root
    }
}
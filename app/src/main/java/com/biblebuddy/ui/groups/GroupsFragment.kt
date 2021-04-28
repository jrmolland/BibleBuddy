package com.biblebuddy.ui.groups

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biblebuddy.R
import com.biblebuddy.SharedViewModel
import com.biblebuddy.data.model.GroupLocation
import com.google.android.gms.maps.model.LatLng

class GroupsFragment : Fragment() {

    private lateinit var model: SharedViewModel
    private lateinit var rvAdapter: RecyclerAdapter
    private lateinit var rv: RecyclerView

    private var groups = mutableListOf<GroupLocation>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        model.fetchGroups()
        val root = inflater.inflate(R.layout.fragment_groups, container, false)

        rv = root.findViewById(R.id.rv_recyclerView)
        rvAdapter = RecyclerAdapter(groups, requireContext())

        initRecyclerView()

        setData()

        return root
    }

    private fun setData() {
        model.groups.observe(
            viewLifecycleOwner,
            Observer { groupLocations ->
                if (groupLocations != null) {
                    rvAdapter.setListData(groupLocations)
                    rvAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Data Error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun initRecyclerView() {
        rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter
        }
    }
}
package com.biblebuddy.ui.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.biblebuddy.R

class GroupsFragment : Fragment() {

    private lateinit var groupsViewModel: GroupsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        groupsViewModel =
                ViewModelProvider(this).get(GroupsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_groups, container, false)

        val listView = root.findViewById<ListView>(R.id.groups_list)
        val groups = arrayOf("John's House", "Living Waters", "Church of Christ")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(root.context, android.R.layout.simple_list_item_1, groups)

        listView.adapter = arrayAdapter

        return root
    }
}
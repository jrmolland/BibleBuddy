package com.biblebuddy.ui.groups

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.biblebuddy.R
import com.biblebuddy.SharedViewModel
import com.biblebuddy.data.model.GroupLocation
import com.biblebuddy.ui.nearby.NearbyMainFragment
import com.biblebuddy.ui.nearby.NearbyMainFragmentArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RecyclerAdapter(private var groups: List<GroupLocation>, private var context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    fun setListData(data: ArrayList<GroupLocation>) {
        groups = data
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.tv_groupName)
        val itemDistance: TextView = itemView.findViewById(R.id.tv_groupDistance)

        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = bindingAdapterPosition
//                Navigation.findNavController(v)
//                    .navigate(GroupsFragmentDirections.nearbyMapFragment(position))

//                var fm: FragmentManager = (context as AppCompatActivity).supportFragmentManager
                val group = groups[position]

                MaterialAlertDialogBuilder(context)
                    .setTitle(group.description)
                    .setMessage("${group.host}\ntel: ${group.phone}")
                    .setPositiveButton("Call Group", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        holder.itemTitle.text = group.description
        holder.itemDistance.text = group.host
    }

    override fun getItemCount(): Int {
        return groups.size
    }
}
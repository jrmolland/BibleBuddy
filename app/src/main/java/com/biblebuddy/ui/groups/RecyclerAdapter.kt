package com.biblebuddy.ui.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.biblebuddy.R
import com.biblebuddy.data.model.GroupLocation

class RecyclerAdapter(private var groups: List<GroupLocation>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

//    fun setListData(data: ArrayList<GroupLocation>) {
//        groups = data
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.tv_groupName)
        val itemDistance: TextView = itemView.findViewById(R.id.tv_groupDistance)

        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = bindingAdapterPosition
                Toast.makeText(
                    itemView.context,
                    "You clicked on item # ${position + 1}",
                    Toast.LENGTH_SHORT
                ).show()
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
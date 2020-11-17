package com.pactera.googlemaptest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pactera.googlemaptest.R
import java.util.*

class MyAdapter(mContent: Context, mList: List<String>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var mList: List<String> = ArrayList()
    private var mContent: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContent).inflate(R.layout.item_nearby_place, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        init {
            tvName = itemView.findViewById(R.id.tvName)
        }
    }

    init {
        this.mList = mList
        this.mContent = mContent
    }
}
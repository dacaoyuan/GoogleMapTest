package com.pactera.googlemaptest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pactera.googlemaptest.model.NearbyModel

class NearbyPlaceAdapter : RecyclerView.Adapter<NearbyPlaceAdapter.ViewHolder>() {

    private var mlist: MutableList<NearbyModel.ResultsDTO>? = null

    init {
        mlist = ArrayList();
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
    }

    public fun setNewData(mlist: MutableList<NearbyModel.ResultsDTO>) {
        this.mlist = mlist;
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_nearby_place, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mlist!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = mlist!![position].name
        holder.tvAddress.text = mlist!![position].vicinity
    }
}
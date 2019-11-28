package com.app.databinding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.databinding.R
import com.app.databinding.databinding.LocationItemBinding
import com.app.databinding.model.StateKt

class LocationItemAdapter(private val mContext: Context, private val mLocationList: List<StateKt>?) :
    RecyclerView.Adapter<LocationItemAdapter.LocationItemAdapterHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationItemAdapterHolder {
        val bindingView =
            DataBindingUtil.inflate<LocationItemBinding>(
                LayoutInflater.from(mContext),
                R.layout.layout_location_item,
                parent,
                false
            )
        return LocationItemAdapterHolder(bindingView)
    }

    override fun getItemCount(): Int {
        return mLocationList?.size!!
    }

    override fun onBindViewHolder(holder: LocationItemAdapterHolder, position: Int) {
        val stateKt = mLocationList?.get(position)
        holder.itemBinding.stateKt = stateKt
        holder.itemBinding.tvLocation.setOnClickListener {
            stateKt?.name = "Changed Name"
            Toast.makeText(mContext, "Clicked on: " + stateKt?.name, Toast.LENGTH_LONG).show()
            notifyItemChanged(position)
        }
    }

    class LocationItemAdapterHolder(binding: LocationItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemBinding: LocationItemBinding

        init {
            itemBinding = binding
        }
    }
}
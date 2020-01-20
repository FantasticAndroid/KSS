package com.sample.android.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.android.R

class DrawerMenuItemAdapter(
    private val context: Context,
    private val menuTitleList: List<String>?,
    menuInterface: OnMenuClickListener
) :
    RecyclerView.Adapter<DrawerMenuItemAdapter.DrawerMenuItemViewHolder>() {

    private var mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mInterface: OnMenuClickListener = menuInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerMenuItemViewHolder {
        val view = mInflater.inflate(R.layout.layout_adapter_drawer_menu_item, parent, false)
        return DrawerMenuItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return menuTitleList?.size!!
    }

    override fun onBindViewHolder(holder: DrawerMenuItemViewHolder, position: Int) {
        val menuTItle = menuTitleList?.get(position)
        holder.tvDrawerMenuTitle.text = menuTItle
        holder.tvDrawerMenuTitle.setOnClickListener {
            menuTItle?.let { title -> mInterface.onMenuItemClicked(title, position) }
        }
    }

    class DrawerMenuItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvDrawerMenuTitle: TextView = view.findViewById(R.id.tvDrawerMenuTitle)
    }

    interface OnMenuClickListener {
        fun onMenuItemClicked(menuTitle: String, position: Int)
    }
}
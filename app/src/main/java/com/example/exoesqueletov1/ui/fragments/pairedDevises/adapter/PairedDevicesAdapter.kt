package com.example.exoesqueletov1.ui.fragments.pairedDevises.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.R
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery

class PairedDevicesAdapter(val list: List<ExoskeletonQuery>) :
    RecyclerView.Adapter<PairedDevicesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PairedDevicesViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bluetooth_devices, parent, false)
    )

    override fun onBindViewHolder(holder: PairedDevicesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}
package com.example.exoesqueletov1.ui.fragments.pairedDevises.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.exoesqueletov1.data.local.query.ExoskeletonQuery
import com.example.exoesqueletov1.databinding.ItemBluetoothDevicesBinding

class PairedDevicesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemBluetoothDevicesBinding.bind(view)
    fun bind(exoskeletonQuery: ExoskeletonQuery) {
        binding.user = exoskeletonQuery
    }
}
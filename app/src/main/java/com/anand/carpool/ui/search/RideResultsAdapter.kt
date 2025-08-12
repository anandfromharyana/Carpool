package com.anand.carpool.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anand.carpool.databinding.ItemRideResultBinding
import com.anand.carpool.model.Ride

class RideResultsAdapter(
    private val onRequestClick: (Ride) -> Unit = {}
) : RecyclerView.Adapter<RideResultsAdapter.RideViewHolder>() {

    private var rideList: List<Ride> = emptyList()

    fun submitList(list: List<Ride>) {
        rideList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideViewHolder {
        val binding = ItemRideResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RideViewHolder, position: Int) {
        val ride = rideList[position]
        holder.bind(ride)
    }

    override fun getItemCount(): Int = rideList.size

    inner class RideViewHolder(private val binding: ItemRideResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ride: Ride) {
            binding.tvFromTo.text = "${ride.from} → ${ride.to}"
            binding.tvDateTime.text = "${ride.date} at ${ride.time}"
            binding.tvSeatsPrice.text = "${ride.seats} seats • ₹${ride.price}"
            // if your item layout has a button id 'btnRequest', set its click:
            binding.root.setOnClickListener { onRequestClick(ride) }
            // if you have a request button:
            // binding.btnRequest.setOnClickListener { onRequestClick(ride) }
        }
    }
}

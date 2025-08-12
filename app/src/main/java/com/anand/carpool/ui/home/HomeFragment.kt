package com.anand.carpool.ui.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anand.carpool.R
import com.anand.carpool.databinding.FragmentHomeBinding
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        // Search button
        binding.btnSearch.setOnClickListener {
            val from = binding.etFrom.text.toString().trim()
            val to = binding.etTo.text.toString().trim()
            val date = binding.tvDate.text.toString().trim()

            Log.d("HomeFragment", "Search clicked - From: '$from', To: '$to', Date: '$date'")

            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter both locations", Toast.LENGTH_SHORT).show()
            } else {
                // Navigate with search parameters
                val bundle = bundleOf(
                    "from" to from,
                    "to" to to,
                    "date" to date
                )
                Log.d("HomeFragment", "Navigating to search results with bundle: $bundle")
                findNavController().navigate(R.id.searchResultsFragment, bundle)
            }
        }

        // Date picker
        binding.tvDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val dateString = "$day-${month + 1}-$year"
                    binding.tvDate.text = dateString
                    Log.d("HomeFragment", "Date selected: $dateString")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time picker
        binding.tvtime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    val timeString = String.format("%02d:%02d", hour, minute)
                    binding.tvtime.text = timeString
                    Log.d("HomeFragment", "Time selected: $timeString")
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Floating action button → Create Ride
        binding.fabCreateRide.setOnClickListener {
            Log.d("HomeFragment", "FAB clicked - navigating to CreateRideFragment")
            findNavController().navigate(R.id.createRideFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
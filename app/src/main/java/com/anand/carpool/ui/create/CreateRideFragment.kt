package com.anand.carpool.ui.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.anand.carpool.databinding.FragmetCreateRideBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CreateRideFragment : Fragment() {

    private var _binding: FragmetCreateRideBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmetCreateRideBinding.inflate(inflater, container, false)

        setupDateTimePickers()
        setupCreateRideButton()

        return binding.root
    }

    private fun setupDateTimePickers() {
        val calendar = Calendar.getInstance()

        // Date picker
        binding.etDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    val dateString = "$day-${month + 1}-$year"
                    binding.etDate.setText(dateString)
                    Log.d("CreateRide", "Date selected: $dateString")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time picker
        binding.etTime.setOnClickListener {
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val timeString = String.format("%02d:%02d", hour, minute)
                binding.etTime.setText(timeString)
                Log.d("CreateRide", "Time selected: $timeString")
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }
    }

    private fun setupCreateRideButton() {
        binding.btnCreateRide.setOnClickListener {
            val from = binding.etFrom.text.toString().trim()
            val to = binding.etTo.text.toString().trim()
            val date = binding.etDate.text.toString().trim()
            val time = binding.etTime.text.toString().trim()
            val seatsText = binding.etSeats.text.toString().trim()
            val priceText = binding.etPrice.text.toString().trim()

            // Simple validation
            if (from.isEmpty() || to.isEmpty() || date.isEmpty() || time.isEmpty() || seatsText.isEmpty() || priceText.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert to numbers
            val seats = seatsText.toIntOrNull()
            val price = priceText.toDoubleOrNull()

            if (seats == null || price == null) {
                Toast.makeText(requireContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("CreateRide", "Creating ride: $from → $to on $date at $time")
            saveRideToFirestore(from, to, date, time, seats, price)
        }
    }

    private fun saveRideToFirestore(from: String, to: String, date: String, time: String, seats: Int, price: Double) {
        val ride = hashMapOf(
            "from" to from,
            "to" to to,
            "date" to date,
            "time" to time,
            "seats" to seats,
            "price" to price,
            "driverId" to "anonymous", // Default driver ID
            "timestamp" to System.currentTimeMillis() // Add timestamp for SearchResults
        )

        Log.d("CreateRide", "Saving ride data: $ride")

        FirebaseFirestore.getInstance().collection("rides")
            .add(ride)
            .addOnSuccessListener { documentReference ->
                Log.d("CreateRide", "Ride saved successfully with ID: ${documentReference.id}")
                Toast.makeText(requireContext(), "Ride created successfully!", Toast.LENGTH_SHORT).show()
                clearForm()
                requireActivity().onBackPressed()
            }
            .addOnFailureListener { e ->
                Log.e("CreateRide", "Error creating ride: ${e.message}", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearForm() {
        binding.etFrom.setText("")
        binding.etTo.setText("")
        binding.etDate.setText("")
        binding.etTime.setText("")
        binding.etSeats.setText("")
        binding.etPrice.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
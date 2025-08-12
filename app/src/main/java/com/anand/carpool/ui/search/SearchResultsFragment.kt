package com.anand.carpool.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anand.carpool.databinding.FragmentSearchResultsBinding
import com.anand.carpool.model.Ride
import com.google.firebase.firestore.FirebaseFirestore

class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RideResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        setupRecyclerView()

        // Read search parameters
        val from = arguments?.getString("from") ?: ""
        val to = arguments?.getString("to") ?: ""
        val date = arguments?.getString("date") ?: ""

        Log.d("SearchResults", "Search params - From: '$from', To: '$to', Date: '$date'")

        // Debug: Log all rides in Firestore
        logAllRides()

        // Fetch matching rides
        fetchSearchResults(from, to, date)

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = RideResultsAdapter { ride ->
            Toast.makeText(requireContext(), "Selected: ${ride.from} → ${ride.to}", Toast.LENGTH_SHORT).show()
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter
    }

    private fun logAllRides() {
        FirebaseFirestore.getInstance().collection("rides")
            .get()
            .addOnSuccessListener { result ->
                Log.d("FirestoreDebug", "=== ALL RIDES IN FIRESTORE ===")
                for (doc in result) {
                    Log.d("FirestoreDebug", "${doc.id} => ${doc.data}")
                }
                Log.d("FirestoreDebug", "Total rides found: ${result.size()}")
                Log.d("FirestoreDebug", "===============================")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreDebug", "Error fetching all rides: ${e.message}")
            }
    }

    private fun fetchSearchResults(from: String?, to: String?, date: String?) {
        val db = FirebaseFirestore.getInstance()

        // Start with base query
        val query = db.collection("rides")

        Log.d("SearchResults", "Starting query with filters:")
        Log.d("SearchResults", "From filter: '${from}' (isEmpty: ${from.isNullOrEmpty()})")
        Log.d("SearchResults", "To filter: '${to}' (isEmpty: ${to.isNullOrEmpty()})")
        Log.d("SearchResults", "Date filter: '${date}' (isEmpty: ${date.isNullOrEmpty()})")

        // For now, let's get all rides and filter manually to debug
        query.get()
            .addOnSuccessListener { result ->
                Log.d("SearchResults", "Query successful. Total rides in DB: ${result.size()}")

                val allRides = result.toObjects(Ride::class.java)
                val filteredRides = allRides.filter { ride ->
                    val fromMatch = from.isNullOrEmpty() || ride.from.equals(from, ignoreCase = true)
                    val toMatch = to.isNullOrEmpty() || ride.to.equals(to, ignoreCase = true)
                    val dateMatch = date.isNullOrEmpty() || ride.date == date

                    Log.d("SearchResults", "Checking ride: ${ride.from} → ${ride.to} on ${ride.date}")
                    Log.d("SearchResults", "From match: $fromMatch, To match: $toMatch, Date match: $dateMatch")

                    fromMatch && toMatch && dateMatch
                }

                Log.d("SearchResults", "Filtered rides count: ${filteredRides.size}")

                if (filteredRides.isEmpty()) {
                    Toast.makeText(requireContext(), "No rides found for your search", Toast.LENGTH_SHORT).show()
                    adapter.submitList(emptyList())
                } else {
                    Toast.makeText(requireContext(), "Found ${filteredRides.size} rides", Toast.LENGTH_SHORT).show()
                    adapter.submitList(filteredRides)
                }
            }
            .addOnFailureListener { e ->
                Log.e("SearchResults", "Query failed: ${e.message}", e)
                Toast.makeText(requireContext(), "Error searching rides: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
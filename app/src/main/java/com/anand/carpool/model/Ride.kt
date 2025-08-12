package com.anand.carpool.model

data class Ride(
    val from: String = "",
    val to: String = "",
    val date: String = "",
    val time: String = "",
    val seats: Int = 0,
    val price: Double = 0.0,
    val driverId: String = "",
    val timestamp: Long = 0L
)
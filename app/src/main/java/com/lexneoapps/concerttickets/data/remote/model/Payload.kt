package com.lexneoapps.concerttickets.data.remote.model

data class Payload(
    val date: String,
    val description: String,
    val discount: Int,
    val id: Int,
    val name: String,
    val photo: String,
    val place: String,
    val price: Double,
    val quantity: Int
)
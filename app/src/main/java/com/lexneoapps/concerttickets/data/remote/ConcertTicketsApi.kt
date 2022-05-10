package com.lexneoapps.concerttickets.data.remote

import com.lexneoapps.concerttickets.data.remote.response.NetworkResponse
import retrofit2.http.GET

interface ConcertTicketsApi {

    @GET("concerts.json")
    suspend fun getConcertTickets() : List<NetworkResponse>

    @GET("concerts.json/d")
    suspend fun getConcertTicketsDiscounted() : List<NetworkResponse>


}
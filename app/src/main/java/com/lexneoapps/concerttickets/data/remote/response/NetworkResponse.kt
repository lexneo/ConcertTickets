package com.lexneoapps.concerttickets.data.remote.response

import com.lexneoapps.concerttickets.data.remote.model.Payload

data class NetworkResponse(
    val payload: Payload,
    val type: String
)
package com.lexneoapps.concerttickets.repository

import android.app.Application
import com.lexneoapps.concerttickets.data.local.DiscountedDao
import com.lexneoapps.concerttickets.data.local.NonDiscountedDao
import com.lexneoapps.concerttickets.data.remote.ConcertTicketsApi
import javax.inject.Inject

class Repository @Inject constructor(
    private val nonDiscountedDao: NonDiscountedDao,
    private val discountedDao: DiscountedDao,
    private val concertTicketsApi: ConcertTicketsApi,
    private val context: Application
)
package com.lexneoapps.concerttickets.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.lexneoapps.concerttickets.data.local.DiscountedDao
import com.lexneoapps.concerttickets.data.local.NonDiscountedDao
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.data.remote.ConcertTicketsApi
import com.lexneoapps.concerttickets.data.remote.response.NetworkResponse
import javax.inject.Inject

class Repository @Inject constructor(
    private val nonDiscountedDao: NonDiscountedDao,
    private val discountedDao: DiscountedDao,
    private val concertTicketsApi: ConcertTicketsApi,
    private val context: Application,


) {

    /*
    * Network response represents object
    * {
    "type": "DISCOUNT",
    "payload": {
      "id": 1,
      "name": "Hans Zimmer",
      "photo": "/images/HansZimmer.jpg",
      "description": "Bla",
      "price": 50.0,
      "quantity": 10,
      "discount": 30,
      "date": "27.07.2022"
    }
  }*/
    suspend fun getAllTickets(): List<NetworkResponse> =
        concertTicketsApi.getConcertTickets()


    suspend fun insertDiscountedTicket(discounted: Discounted) =
        discountedDao.insertDiscounted(discounted)

    suspend fun insertNonDiscountedTicket(nonDiscounted: NonDiscounted) =
        nonDiscountedDao.insertNonDiscounted(nonDiscounted)

    suspend fun deleteAllTickets() {
        nonDiscountedDao.deleteAllNonDiscounted()
        discountedDao.deleteAllDiscounted()
    }

     fun getAllDiscounted() : LiveData<List<Discounted>> =
        discountedDao.getAllDiscountedOrderedByDate()

    suspend fun getDiscountedById(id: Int) =
        discountedDao.getDiscountedById(id)

    suspend fun getNonDiscountedById(id: Int) =
        nonDiscountedDao.getNonDiscountedById(id)


    fun getAllNonDiscounted() : LiveData<List<NonDiscounted>> =
        nonDiscountedDao.getAllNonDiscountedOrderedByDate()

    fun getAllDiscountedExpired(dateString: String) : LiveData<List<Discounted>> =
        discountedDao.getAllDiscountedExpired(dateString)


     fun get2NonDiscounteds(dateString: String) : LiveData<List<NonDiscounted>> =
        nonDiscountedDao.get2NonDiscountedOrderedByUpcomings(dateString)

    suspend fun deleteDiscounted(discounted: Discounted) =
        discountedDao.deleteDiscounted(discounted)

    suspend fun deleteNonDiscounted(nonDiscounted: NonDiscounted) =
        nonDiscountedDao.deleteNonDiscounted(nonDiscounted)

    suspend fun updateDiscounted(discounted: Discounted) =
        discountedDao.updateDiscounted(discounted)

    suspend fun updateNonDiscounted(nonDiscounted: NonDiscounted) =
        nonDiscountedDao.updateNonDiscounted(nonDiscounted)


}




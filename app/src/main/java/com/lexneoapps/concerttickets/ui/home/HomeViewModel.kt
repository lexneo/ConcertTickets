package com.lexneoapps.concerttickets.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.internal.bind.util.ISO8601Utils
import com.lexneoapps.concerttickets.data.local.PreferencesManager
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.di.ApplicationScope
import com.lexneoapps.concerttickets.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

private const val COMMON_TAG = "Combined"
private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager,
    @ApplicationScope private val applicationScope: CoroutineScope

) : ViewModel() {

    val discountedTickets = repository.getAllDiscounted()
    val nonDiscounted2Tickets = repository.get2NonDiscounteds(getTodayIsoDate())
    val expiredDiscounted = repository.getAllDiscountedExpired(getTodayIsoDate())

    //if is first opening of the app - download tickets from api
    fun getTicketsFromApiIfNeeded() {
        applicationScope.launch {
            if (isFirstOpen()) {
                Log.i(TAG, "getTicketsFromApiIfNeeded: is called")
                getAllTicketsAndInsertItToDatabase()
            }
        }
    }

    private suspend fun getAllTicketsAndInsertItToDatabase() {
        applicationScope.launch {
            Log.i(TAG, "getAllTicketsAndInsertItToDatabase: adding to database")
            val allTickets = repository.getAllTickets()
            for (ticket in allTickets) {
                if (ticket.type == "DISCOUNT") {

                    launch {
                        ticket.payload.apply {
                            val discounted = Discounted(
                                this.name,
                                formatDate(this.date),
                                this.description,
                                this.photo,
                                this.place,
                                this.price,
                                this.quantity,
                                this.discount,
                                this.id
                            )
                            repository.insertDiscountedTicket(discounted)
                        }
                    }
                } else {
                    launch {
                        ticket.payload.apply {
                            val nonDiscounted = NonDiscounted(
                                this.name,
                                formatDate(this.date),
                                this.description,
                                this.photo,
                                this.place,
                                this.price,
                                this.quantity,
                                this.id
                            )
                            repository.insertNonDiscountedTicket(nonDiscounted)
                        }
                    }
                }
            }
        }
        setIsFirstOpen(false)
    }

    private fun formatDate(dateString: String): String {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val localDate = LocalDate.parse(dateString, inputFormatter)
            val outputFormatter = DateTimeFormatter.ISO_DATE_TIME
            val localDateTime = localDate.atStartOfDay()
            outputFormatter.format(localDateTime)

        } else {
            val parser = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            formatter.format(parser.parse(dateString)!!)
        }
    }

    private fun getTodayIsoDate(): String {

        val todayDate = Calendar.getInstance().time

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy")
            val localDate = LocalDate.parse(todayDate.toString(), inputFormatter)
            val outputFormatter = DateTimeFormatter.ISO_DATE_TIME
            val localDateTime = localDate.atStartOfDay()
            outputFormatter.format(localDateTime)
        } else {
            val parser = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            return formatter.format(parser.parse(todayDate.toString())!!)
        }
    }


    private suspend fun isFirstOpen() = preferencesManager.firstOpen.first()

    private fun setIsFirstOpen(isFirstOpen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesManager.isFirstOpen(isFirstOpen)
        }
    }
}

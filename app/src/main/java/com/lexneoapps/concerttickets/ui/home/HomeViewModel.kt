package com.lexneoapps.concerttickets.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

private const val TAG = "HomeViewModel"
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager,
    @ApplicationScope private val applicationScope: CoroutineScope

) : ViewModel() {



    val discountedTickets = repository.getAllDiscounted()
/*//    val nonDiscountedTickets = repository.getAllNonDiscounted()
var discountedUpcoming : List<Discounted> = mutableListOf()


    private val _finishedInserting: MutableLiveData<Boolean> = MutableLiveData(false)
    val finishedInserting: LiveData<Boolean>
        get() = _finishedInserting

    private val _upcoming: MutableLiveData<List<Discounted>> = MutableLiveData()
    val upcoming: LiveData<List<Discounted>>
        get() = _upcoming*/





    //if is first opening of the app - download tickets from api
    fun getTicketsFromApiIfNeeded(){
        applicationScope.launch {
            if (isFirstOpen()){
                Log.i(TAG, "getTicketsFromApiIfNeeded: is called")
                getAllTicketsAndInsertItToDatabase()
//                _upcoming.value = repository.getAllDiscountedUpcoming()

            }
        }
//        _finishedInserting.value = true
    }








    private suspend fun getAllTicketsAndInsertItToDatabase() {

        Log.i(TAG, "getAllTicketsAndInsertItToDatabase: adding to database")
            val allTickets = repository.getAllTickets()
            for (ticket in allTickets) {
                if (ticket.type == "DISCOUNT") {
                    ticket.payload.apply {
                        val discounted = Discounted(
                            this.id,
                            this.date,
                            this.description,
                            this.discount,
                            this.name,
                            this.photo,
                            this.place,
                            this.price,
                            this.quantity
                        )
                        repository.insertDiscountedTicket(discounted)
                    }
                }else{
                    ticket.payload.apply {
                        val nonDiscounted = NonDiscounted(
                            this.id,
                            this.date,
                            this.description,
                            this.name,
                            this.photo,
                            this.place,
                            this.price,
                            this.quantity
                        )
                        repository.insertNonDiscountedTicket(nonDiscounted)
                        /*
                        * idea for date
                        * LocalDate.parse(this.date, DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                        */

                    }
                }
            }


            setIsFirstOpen(false)
        }






    private suspend fun isFirstOpen() = preferencesManager.firstOpen.first()

    private fun setIsFirstOpen(isFirstOpen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
          preferencesManager.isFirstOpen(isFirstOpen)
        }
    }




}

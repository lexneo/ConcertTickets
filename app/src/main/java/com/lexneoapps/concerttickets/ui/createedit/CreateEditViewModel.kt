package com.lexneoapps.concerttickets.ui.createedit

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


private const val TAG = "CreateEditViewModel"
const val CREATE_COMBINED = "CREATE_COMBINED"

@HiltViewModel
class CreateEditViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _discounted: MutableLiveData<Discounted?> = MutableLiveData()
    val discounted: LiveData<Discounted?>
        get() = _discounted

    private var _nonDiscounted: MutableLiveData<NonDiscounted?> = MutableLiveData()
    val nonDiscounted: LiveData<NonDiscounted?>
        get() = _nonDiscounted


    private val _editEvent: MutableLiveData<EditEvent> = MutableLiveData()
    val editEvent: LiveData<EditEvent>
        get() = _editEvent

    private val _finished: MutableLiveData<Boolean> = MutableLiveData()
    val finished: LiveData<Boolean>
        get() = _finished




    fun getTicketsById(id: Int) {
        val job = viewModelScope.launch {
            launch {
                Log.i(CREATE_COMBINED, "VM getNonDis started")
                val nonDiscounted = repository.getNonDiscountedById(id)
                nonDiscounted?.let {
                    withContext(Dispatchers.Main) {
                        _nonDiscounted.value = it
                    }
                }
                Log.i(CREATE_COMBINED, "VM getNonDis finished ${nonDiscounted?.id}")
            }
            launch {
                Log.i(CREATE_COMBINED, "VM getDis started")

                val discounted = repository.getDiscountedById(id)
                discounted?.let {
                    withContext(Dispatchers.Main) {
                        _discounted.value = it
                    }
                }
                Log.i(CREATE_COMBINED, "VM getDis finished ${discounted?.id}")
            }
        }
        job.invokeOnCompletion {
            _finished.value = true
            Log.i(CREATE_COMBINED, "VM getTicketsById finished")
        }
    }

    fun editTickets(
        discountedQuantity: Int,
        nonDiscountedQuantity: Int,
        totalQuantity: Int,
        discountValue: Int
    ) {
        if ((discountedQuantity + nonDiscountedQuantity) != totalQuantity) {
            _editEvent.value = EditEvent.WRONG_PARAMETERS
        } else if (_discounted.value != null && _nonDiscounted.value != null) {
            val updatedDiscounted = _discounted.value!!.copy(quantity = discountedQuantity)
            updateDiscounted(updatedDiscounted)
            val updatedNonDiscounted = _nonDiscounted.value!!.copy(quantity = nonDiscountedQuantity)
            updateNonDiscounted(updatedNonDiscounted)
            _editEvent.value = EditEvent.UPDATED_BOTH

        } else if (_discounted.value != null && _nonDiscounted.value == null && nonDiscountedQuantity > 0) {
            val updatedDiscounted = _discounted.value!!.copy(quantity = discountedQuantity)
            updateDiscounted(updatedDiscounted)
            val d = _discounted.value!!
            val createdNonDiscounted = NonDiscounted(
                d.name,
                d.date,
                d.description,
                d.photo,
                d.place,
                d.price,
                nonDiscountedQuantity,
                d.id
            )
            createNonDiscounted(createdNonDiscounted)
            _editEvent.value = EditEvent.UP_DISC_CRE_NONDISC

        } else if (_discounted.value == null && _nonDiscounted.value != null && discountedQuantity > 0) {
            val updatedNonDiscounted = _nonDiscounted.value!!.copy(quantity = nonDiscountedQuantity)
            updateNonDiscounted(updatedNonDiscounted)
            val nD = _nonDiscounted.value!!
            val createdDiscounted = Discounted(
                nD.name,
                nD.date,
                nD.description,
                nD.photo,
                nD.place,
                nD.price,
                discountedQuantity,
                discountValue,
                nD.id
            )
            createDiscounted(createdDiscounted)
            _editEvent.value = EditEvent.UP_NONDISC_CRE_DISC
        }

    }


    private fun updateDiscounted(discounted: Discounted) = viewModelScope.launch {
        repository.updateDiscounted(discounted)
    }

    private fun updateNonDiscounted(nonDiscounted: NonDiscounted) = viewModelScope.launch {
        repository.updateNonDiscounted(nonDiscounted)
    }

    private fun createNonDiscounted(nonDiscounted: NonDiscounted) = viewModelScope.launch {
        repository.insertNonDiscountedTicket(nonDiscounted)
    }

    private fun createDiscounted(discounted: Discounted) = viewModelScope.launch {
        repository.insertDiscountedTicket(discounted)
    }

    fun createTickets(
        name: String, date: String, description: String,
        place: String, price: Double?, quantity: Int?,
        discount: Int?, discountQuantity: Int?
    ) {
        if (name.isEmpty() || date.isEmpty() || place.isEmpty() ||
            (price == 0.0 || price == null) || (quantity == 0 && discountQuantity == null) ||
            (discount == null && discountQuantity != null)
            || (discount != null && discountQuantity == null)
        ) {
            _editEvent.value = EditEvent.EMPTY_PARAMETER
        } else if (discount != null && (discountQuantity != null && discountQuantity > 0)) {
            val createdDiscounted = Discounted(
                name,
                date,
                description,
                "/images/Concert.jpg",
                place,
                price,
                discountQuantity,
                discount
            )
            createDiscounted(createdDiscounted)
            if (quantity != null && quantity > 0) {
                val createdNonDiscounted = NonDiscounted(
                    name,
                    date,
                    description,
                    "/images/Concert.jpg",
                    place,
                    price,
                    quantity
                )
                createNonDiscounted(createdNonDiscounted)
                _editEvent.value = EditEvent.CREATED_NEW_TICKETS

            }
        } else if (discount == null && (discountQuantity == null || discountQuantity == 0)) {
            val createdNonDiscounted = NonDiscounted(
                name,
                date,
                description,
                "/images/Concert.jpg",
                place,
                price,
                quantity
            )
            createNonDiscounted(createdNonDiscounted)
            _editEvent.value = EditEvent.CREATED_NEW_TICKETS

        }

    }


}


/*fun onSaveClick(
    nonDiscounted: NonDiscounted?,
    discounted: Discounted?,
    name: String,
    date: String,
    description: String,
    place: String,
    price: Double?,
    quantity: Int?,
    discount: Int?,
    discountQuantity: Int?
) {
    if (name.isEmpty() || date.isEmpty() || place.isEmpty() || price == null || quantity == null) {
        _onSaveEvent.value = SaveEvent.EMPTY_PARAMETER
    } else if (discountQuantity != null && discountQuantity > quantity) {
        _onSaveEvent.value = SaveEvent.DISCOUNT_OVER_MAX
    } else if (nonDiscounted != null && discount == null) {
        viewModelScope.launch {
            repository.insertNonDiscountedTicket(
                nonDiscounted.copy(
                    name = name,
                    date = date,
                    description = description,
                    place = place,
                    price = price,
                    quantity = quantity
                )
            )
        }
        _onSaveEvent.value = SaveEvent.EDITED_NONDISCOUNTED


    } else if (nonDiscounted != null && discount != null) {
        val totalNonDiscounted = quantity - discountQuantity!!
        viewModelScope.launch {
            repository.insertNonDiscountedTicket(
                nonDiscounted.copy(
                    name = name,
                    date = date,
                    description = description,
                    place = place,
                    price = price,
                    quantity = totalNonDiscounted
                )
            )
            launch {
                repository.insertDiscountedTicket(
                    Discounted(
                        name = name,
                        date = date,
                        description = description,
                        place = place,
                        price = price,
                        quantity = discountQuantity,
                        photo = nonDiscounted.photo,
                        discount = discount,
                        id = nonDiscounted.id
                    )
                )
            }
        }
        _onSaveEvent.value = SaveEvent.EDITED_NONDISCOUNTED_CREATED_DISCOUNTED

    } else if ((discountQuantity == quantity) && discounted != null) {
        viewModelScope.launch {
            repository.insertDiscountedTicket(
                discounted.copy(
                    name = name,
                    date = date,
                    description = description,
                    place = place,
                    price = price,
                    quantity = quantity,
                    discount = discount
                )
            )
        }
        _onSaveEvent.value = SaveEvent.EDITED_DISCOUNTED
    } else if ((discountQuantity!! < quantity) && discounted != null) {
        val totalNonDiscounted = quantity - discountQuantity!!
        viewModelScope.launch {
            repository.insertDiscountedTicket(
                discounted.copy(
                    name = name,
                    date = date,
                    description = description,
                    place = place,
                    price = price,
                    quantity = discountQuantity,
                    discount = discount
                )
            )
            launch {
                repository.insertNonDiscountedTicket(
                    NonDiscounted(
                        name = name,
                        date = date,
                        description = description,
                        place = place,
                        price = price,
                        quantity = totalNonDiscounted,
                        photo = discounted.photo,
                        id = discounted.id
                    )
                )
            }
        }
        _onSaveEvent.value = SaveEvent.EDITED_DISCOUNTED_CREATED_NONDISCOUNTED

    }else if (non)
}
*/



enum class EditEvent {
    EMPTY_PARAMETER,
    WRONG_PARAMETERS,
    UPDATED_BOTH,
    UP_DISC_CRE_NONDISC,
    UP_NONDISC_CRE_DISC,
    CREATED_NEW_TICKETS,



}




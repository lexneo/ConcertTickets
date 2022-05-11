package com.lexneoapps.concerttickets.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexneoapps.concerttickets.data.local.NonDiscountedDao
import com.lexneoapps.concerttickets.data.local.PreferencesManager
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.di.ApplicationScope
import com.lexneoapps.concerttickets.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val repository: Repository,
    private val preferencesManager: PreferencesManager,
    @ApplicationScope private val applicationScope: CoroutineScope

) : ViewModel() {

    val discountedTickets = repository.getAllDiscounted()
    val nonDiscountedTickets = repository.getAllNonDiscounted()

    private val _discounted: MutableLiveData<Discounted?> = MutableLiveData()
    val discounted: LiveData<Discounted?>
        get() = _discounted

    private val _nonDiscounted: MutableLiveData<NonDiscounted?> = MutableLiveData()
    val nonDiscounted: LiveData<NonDiscounted?>
        get() = _nonDiscounted

    fun setDiscounted(discounted: Discounted){
        _discounted.value = discounted
        _nonDiscounted.value = null
    }

    fun setNonDiscounted(nonDiscounted: NonDiscounted){
        _nonDiscounted.value = nonDiscounted
        _discounted.value = null
    }

    fun onConfirmClick() = viewModelScope.launch {
        if (_discounted.value != null){
            discounted.value?.let { repository.deleteDiscounted(it) }
        }else{
            nonDiscounted.value?.let { repository.deleteNonDiscounted(it) }
        }
    }


    //bonus 1
    fun resetEverything(){
        applicationScope.launch {
            preferencesManager.isFirstOpen(true)
            repository.deleteAllTickets()
            delay(500)
        }
    }

}

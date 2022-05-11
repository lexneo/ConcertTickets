package com.lexneoapps.concerttickets.ui.createedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lexneoapps.concerttickets.data.local.NonDiscountedDao
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted
import com.lexneoapps.concerttickets.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
     private val repository: Repository
) : ViewModel() {

     private val _discounted: MutableLiveData<Discounted?> = MutableLiveData()
     val discounted: LiveData<Discounted?>
          get() = _discounted

     private val _nonDiscounted: MutableLiveData<NonDiscounted?> = MutableLiveData()
     val nonDiscounted: LiveData<NonDiscounted?>
          get() = _nonDiscounted



}

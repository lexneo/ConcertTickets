package com.lexneoapps.concerttickets.ui.createedit

import androidx.lifecycle.ViewModel
import com.lexneoapps.concerttickets.data.local.NonDiscountedDao
import com.lexneoapps.concerttickets.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel @Inject constructor(
     private val repository: Repository
) : ViewModel() {

}

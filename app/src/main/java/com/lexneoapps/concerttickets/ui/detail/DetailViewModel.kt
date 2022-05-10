package com.lexneoapps.concerttickets.ui.detail

import androidx.lifecycle.ViewModel
import com.lexneoapps.concerttickets.data.local.NonDiscountedDao
import com.lexneoapps.concerttickets.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
     private val repository: Repository
) : ViewModel() {

}

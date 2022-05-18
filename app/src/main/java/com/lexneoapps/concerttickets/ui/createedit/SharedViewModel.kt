package com.lexneoapps.concerttickets.ui.createedit

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lexneoapps.concerttickets.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _time: MutableLiveData<String> = MutableLiveData()
    val time: LiveData<String>
        get() = _time

    fun setTime(chosenDate: String) {
        _time.value = formatDate(chosenDate)

    }
    private fun formatDate(dateString: String): String {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
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
}
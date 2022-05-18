package com.lexneoapps.concerttickets.ui.createedit

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import java.text.DateFormat
import java.util.*

private const val TAG = "DatePickerFragment"
class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    val calender = Calendar.getInstance()

    private val viewModel: CreateEditViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)



        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        calender.set(Calendar.YEAR,year)
        calender.set(Calendar.MONTH,month)
        calender.set(Calendar.DAY_OF_MONTH,day)


        val millies = calender.timeInMillis
        val chosenDate = DateFormat.getDateInstance().format(millies)
        viewModel.setTime(chosenDate)

        Log.i(TAG, "onDateSet: $chosenDate ")



    }
}
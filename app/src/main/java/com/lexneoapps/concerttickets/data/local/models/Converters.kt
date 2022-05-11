package com.lexneoapps.concerttickets.data.local.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@ProvidedTypeConverter
class Converters {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(dateString: String) : LocalDate? {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    }

    @TypeConverter
    fun fromDate(date: Date) : String{
        return DateFormat.getDateInstance().format(date)
    }


}
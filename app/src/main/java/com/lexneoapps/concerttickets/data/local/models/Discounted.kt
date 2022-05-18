package com.lexneoapps.concerttickets.data.local.models

import android.os.Build
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

@Entity(tableName = "discounted_table")
@Parcelize
data class Discounted(

    val name: String?,
    val date: String?,
    val description: String?,
    val photo: String?,
    val place: String?,
    val price: Double?,
    val quantity: Int?,
    val discount: Int?,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) : Parcelable {



    val percentage: Int
        get() = (100 * ((price!! - discount!!) / price)).roundToInt()

    val discountDifference: Double?
        get() = price?.minus(discount?.toDouble()!!)

    @IgnoredOnParcel
    lateinit var month :String
    @IgnoredOnParcel
    lateinit var day : String
    @IgnoredOnParcel
    lateinit var year: String
    @IgnoredOnParcel
    lateinit var dateShort: String
    @IgnoredOnParcel
    lateinit var time: String

    init {
        if (date != null) {
            sepDate(date)
        }
    }





    private fun sepDate(dateString: String){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ISO_DATE_TIME
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val date = formatter.parse(dateString)
            month = DateTimeFormatter.ofPattern("MMM").format(date)
            day = DateTimeFormatter.ofPattern("dd").format(date)
            year = DateTimeFormatter.ofPattern("yyyy").format(date)
            dateShort = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm a").format(date)
            time = DateTimeFormatter.ofPattern("HH:mm a").format(date)

        } else {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = formatter.parse(dateString)
            month = SimpleDateFormat("MMM", Locale.getDefault()).format(date)
            day = SimpleDateFormat("dd", Locale.getDefault()).format(date)
            year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            dateShort = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
            time = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(date)

        }

    }


}
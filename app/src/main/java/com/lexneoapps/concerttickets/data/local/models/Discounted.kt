package com.lexneoapps.concerttickets.data.local.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import kotlin.math.roundToInt

@Entity(tableName = "discounted_table")
@Parcelize
data class Discounted(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val date: String?,
    val description: String?,
    val discount: Int?,
    val name: String?,
    val photo: String?,
    val place: String?,
    val price: Double?,
    val quantity: Int?
) : Parcelable {



    val percentage: Int
        get() = (100 * ((price!! - discount!!) / price) ).roundToInt()

    val discountDifference: Double?
        get() = price?.minus(discount?.toDouble()!!)

    val formatedDate: String
        get() = DateFormat.getDateInstance().format(date)




}
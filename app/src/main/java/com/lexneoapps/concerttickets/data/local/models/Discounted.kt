package com.lexneoapps.concerttickets.data.local.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

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
) : Parcelable
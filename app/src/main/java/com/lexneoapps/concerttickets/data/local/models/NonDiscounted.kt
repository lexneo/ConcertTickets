package com.lexneoapps.concerttickets.data.local.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "non_discounted_table")
@Parcelize
data class NonDiscounted(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val date: String?,
    val description: String?,
    val name: String?,
    val photo: String?,
    val place: String?,
    val price: Double?,
    val quantity: Int?
) : Parcelable
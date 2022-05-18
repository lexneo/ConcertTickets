package com.lexneoapps.concerttickets.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted

@Database(
    entities =[Discounted::class,NonDiscounted::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun nonDiscountedDao() : NonDiscountedDao
    abstract fun discountedDao() : DiscountedDao
}
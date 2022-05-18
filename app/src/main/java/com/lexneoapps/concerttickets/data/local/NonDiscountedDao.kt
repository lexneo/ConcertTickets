package com.lexneoapps.concerttickets.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lexneoapps.concerttickets.data.local.models.Discounted
import com.lexneoapps.concerttickets.data.local.models.NonDiscounted

@Dao
interface NonDiscountedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNonDiscounted(nonDiscounted: NonDiscounted)

    @Update
    suspend fun updateNonDiscounted(nonDiscounted: NonDiscounted)

    @Delete
    suspend fun deleteNonDiscounted(nonDiscounted: NonDiscounted)

    @Query("DELETE FROM non_discounted_table")
    suspend fun deleteAllNonDiscounted()

    @Query("SELECT * FROM non_discounted_table WHERE id =:id")
    suspend fun getNonDiscountedById(id: Int) : NonDiscounted?

    @Query("SELECT * FROM non_discounted_table ORDER BY date ASC")
     fun getAllNonDiscountedOrderedByDate() : LiveData<List<NonDiscounted>>

    @Query("SELECT * FROM non_discounted_table WHERE date >=:dateToday ORDER BY date ASC LIMIT 2")
    suspend fun get2NonDiscountedOrderedByUpcoming(dateToday : String) : List<NonDiscounted>

    @Query("SELECT * FROM non_discounted_table WHERE date >=:dateToday ORDER BY date ASC LIMIT 2")
    fun get2NonDiscountedOrderedByUpcomings(dateToday : String) : LiveData<List<NonDiscounted>>


}
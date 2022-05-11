package com.lexneoapps.concerttickets.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("SELECT * FROM non_discounted_table ORDER BY date ASC")
     fun getAllNonDiscountedOrderedByDate() : LiveData<List<NonDiscounted>>

}
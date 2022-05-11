package com.lexneoapps.concerttickets.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lexneoapps.concerttickets.data.local.models.Discounted

@Dao
interface DiscountedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscounted(discounted: Discounted)

    @Update
    suspend fun updateDiscounted(discounted: Discounted)

    @Delete
    suspend fun deleteDiscounted(discounted: Discounted)

    @Query("DELETE FROM discounted_table")
    suspend fun deleteAllDiscounted()

    @Query("SELECT * FROM discounted_table ORDER BY date ASC")
     fun getAllDiscountedOrderedByDate() : LiveData<List<Discounted>>

    @Query("SELECT * FROM discounted_table ORDER BY date ASC")
    suspend fun getAllDiscountedOrderedByUpcoming() : List<Discounted>


}
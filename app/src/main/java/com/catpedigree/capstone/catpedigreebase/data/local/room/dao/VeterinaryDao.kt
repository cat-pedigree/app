package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.VeterinaryItems

@Dao
interface VeterinaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVeterinary(comments: List<VeterinaryItems>)

    @Query("SELECT * FROM veterinary_items")
    fun getVeterinary(): LiveData<List<VeterinaryItems>>

    @Query("DELETE FROM veterinary_items")
    suspend fun deleteAllVeterinary()
}
package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.CatItems

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cats: List<CatItems>)

    @Query("SELECT * FROM cat_items WHERE user_id = :user_id")
    fun getCats(user_id: Int?): LiveData<List<CatItems>>

    @Query("SELECT * FROM cat_items")
    fun allCats(): LiveData<List<CatItems>>

    @Query("SELECT count(*) FROM cat_items WHERE user_id = :user_id")
    fun checkCat(user_id: Int?): LiveData<Int>

    @Query("DELETE FROM cat_items")
    suspend fun deleteAllCats()
}
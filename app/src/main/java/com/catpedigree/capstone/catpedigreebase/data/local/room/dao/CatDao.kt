package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.catpedigree.capstone.catpedigreebase.data.network.item.CatItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems

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

    @Query("SELECT * FROM cat_items WHERE isSelected = 1")
    fun getCatSelected(): LiveData<CatItems>

    @Update
    suspend fun updateCat(cat: CatItems)

    @Query("SELECT EXISTS(SELECT * FROM cat_items WHERE id = :id AND isSelected = 1)")
    suspend fun isCatSelected(id: Int): Boolean

    @Query("DELETE FROM cat_items")
    suspend fun deleteAllCats()
}
package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.AlbumItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.CatItems

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cats: List<CatItems>)

    @Query("SELECT * FROM cat_items WHERE user_id = :user_id")
    fun getCats(user_id: Int?): LiveData<List<CatItems>>

    @Query("SELECT count(*) FROM cat_items WHERE user_id = :user_id")
    fun checkCat(user_id: Int?): LiveData<Int>

    @Query("DELETE FROM cat_items")
    suspend fun deleteAllCats()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: List<AlbumItems>)

    @Query("SELECT * FROM album_items WHERE user_id = :user_id AND cat_id = :cat_id")
    fun getAlbums(user_id: Int?, cat_id: Int?): LiveData<List<AlbumItems>>

    @Query("DELETE FROM album_items")
    suspend fun deleteAllAlbums()
}
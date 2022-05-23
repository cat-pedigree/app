package com.catpedigree.capstone.catpedigreebase.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.item.FavoriteItems

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addToFavorite(favoritePost: FavoriteItems)

    @Query("SELECT * FROM favorite_post")
    fun getFavoritePost(): LiveData<List<FavoriteItems>>

    @Query("SELECT count(*) FROM favorite_post WHERE favorite_post.id = :id")
    suspend fun checkPost(id: Int): Int

    @Query("DELETE FROM favorite_post WHERE favorite_post.id = :id")
    suspend fun removeFromFavorite(id: Int): Int
}
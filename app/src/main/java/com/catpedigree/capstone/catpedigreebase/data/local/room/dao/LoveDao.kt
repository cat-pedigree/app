package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.LoveItems

@Dao
interface LoveDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLove(loveItems: LoveItems)

    @Query("SELECT * FROM love_items WHERE post_id = :post_id")
    fun getLoves(post_id: Int?): PagingSource<Int, LoveItems>

    @Query("SELECT count(*) FROM love_items WHERE love_items.id = :id")
    suspend fun checkLove(id: Int): Int

    @Query("DELETE FROM love_items WHERE love_items.post_id = :post_id AND love_items.user_id = :user_id")
    suspend fun removeFromLove(post_id: Int, user_id: Int): Int

    @Query("DELETE FROM love_items")
    suspend fun deleteAllLoves()
}
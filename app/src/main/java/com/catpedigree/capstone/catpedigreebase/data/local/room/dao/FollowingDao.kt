package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.FollowItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.FollowingItems

@Dao
interface FollowingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowing(follow: List<FollowingItems>)

    @Query("SELECT * FROM following_items WHERE follower_id = :follower_id")
    fun getFollowing(follower_id: Int?): LiveData<List<FollowingItems>>

    @Query("DELETE FROM following_items")
    suspend fun deleteAllFollowing()
}
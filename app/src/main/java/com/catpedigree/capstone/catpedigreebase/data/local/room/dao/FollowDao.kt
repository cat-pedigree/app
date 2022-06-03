package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.FollowItems

@Dao
interface FollowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: List<FollowItems>)

    @Query("SELECT * FROM follow_items WHERE user_id = :user_id")
    fun getFollower(user_id: Int?): LiveData<List<FollowItems>>

    @Query("SELECT * FROM follow_items WHERE follower_id = :follower_id")
    fun getFollowing(follower_id: Int?): LiveData<List<FollowItems>>

    @Query("SELECT COUNT(*) FROM follow_items WHERE user_id = :user_id AND follower_id= :follower_id")
    fun getCheckFollow(user_id: Int?, follower_id: Int): LiveData<Int>

    @Query("SELECT count(*) FROM follow_items WHERE user_id = :user_id AND follower_id = :follower_id")
    fun checkFollow(user_id: Int?, follower_id: Int): LiveData<Int>

    @Query("DELETE FROM follow_items")
    suspend fun deleteAllFollows()

    @Query("DELETE FROM follow_items WHERE follower_id = :follower_id")
    suspend fun deleteFollower(follower_id: Int)
}
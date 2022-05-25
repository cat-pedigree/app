package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<PostItems>)

    @Query("SELECT * FROM post_items")
    fun getPosts(): PagingSource<Int, PostItems>

    @Query("DELETE FROM post_items")
    suspend fun deleteAllPosts()
}
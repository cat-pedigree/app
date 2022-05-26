package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: List<PostItems>)

    @Query("SELECT * FROM post_items ORDER BY created_at DESC")
    fun getPosts(): LiveData<List<PostItems>>

    @Query("DELETE FROM post_items WHERE bookmarked = 0")
    suspend fun deleteAllPosts()

    @Update
    suspend fun updatePost(post: PostItems)

    @Query("SELECT EXISTS(SELECT * FROM post_items WHERE id = :id AND bookmarked = 1)")
    suspend fun isPostsBookmarked(id: Int): Boolean
}
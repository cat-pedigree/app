package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: List<PostItems>)

    @Query("SELECT * FROM post_items ORDER BY created_at DESC")
    fun getPosts(): LiveData<List<PostItems>>

    @Query("SELECT * FROM post_items WHERE user_id = :user_id ORDER BY created_at DESC")
    fun getPostProfile(user_id: Int): LiveData<List<PostItems>>

    @Query("SELECT * FROM post_items WHERE bookmarked = 1")
    fun getPostFavorite():LiveData<List<PostItems>>

    @Query("SELECT COUNT(*) FROM post_items WHERE bookmarked = 1")
    fun getPostFavoriteCount(): LiveData<Int>

    @Query("DELETE FROM post_items")
    suspend fun deleteAllPosts()

    @Query("DELETE FROM post_items WHERE user_id = :user_id")
    suspend fun deleteAllPostsUser(user_id: Int)

    @Update
    suspend fun updatePost(post: PostItems)

    @Query("SELECT EXISTS(SELECT * FROM post_items WHERE id = :id AND bookmarked = 1)")
    suspend fun isPostsBookmarked(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM post_items WHERE id = :id AND loved = 1)")
    suspend fun isPostsLoved(id: Int): Boolean
}
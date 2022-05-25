package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.CommentItems

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comments: List<CommentItems>)

    @Query("SELECT * FROM comment_items WHERE post_id = :post_id")
    fun getComments(post_id: Int?): PagingSource<Int, CommentItems>

    @Query("DELETE FROM comment_items")
    suspend fun deleteAllComments()
}
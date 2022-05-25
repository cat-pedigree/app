package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.RemoteCommentItems

@Dao
interface RemoteCommentItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteCommentItems>)

    @Query("SELECT * FROM remote_comment_items WHERE id = :id")
    suspend fun getRemoteKeysById(id: Int?): RemoteCommentItems?

    @Query("DELETE FROM remote_comment_items")
    suspend fun deleteRemoteKeys()
}
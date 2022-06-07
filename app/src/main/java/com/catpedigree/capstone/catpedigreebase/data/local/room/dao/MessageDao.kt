package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.MessageItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.RoomMessageItems

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: List<MessageItems>)

    @Query("SELECT * FROM message_items WHERE room_id = :room_id")
    fun getMessage(room_id: Int?): LiveData<List<MessageItems>>

    @Query("DELETE FROM message_items")
    suspend fun deleteAllMessage()
}
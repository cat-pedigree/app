package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.MessageRoomItems

@Dao
interface MessageRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageRoom(messageRooms: List<MessageRoomItems>)

    @Query("SELECT * FROM message_room_items WHERE sender_user_id = :sender_user_id")
    fun getMessageRoom(sender_user_id: Int?): LiveData<List<MessageRoomItems>>

    @Query("DELETE FROM message_room_items")
    suspend fun deleteAllMessageRoom()
}
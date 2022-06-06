package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.MessageRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.MessageRoomItems
import com.catpedigree.capstone.catpedigreebase.utils.Result

class MessageRepository(
    private val messageRemoteDataSource: MessageRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    fun getMessageRoom(token: String, sender_user_id: Int): LiveData<Result<List<MessageRoomItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = messageRemoteDataSource.getMessageRoom(token, sender_user_id)
            val rooms = response.body()?.data
            val newRoomMessage = rooms?.map {
                MessageRoomItems(
                    it.id,
                    it.receiver_user_id,
                    it.sender_user_id,
                    it.messages,
                    it.user?.name,
                    it.user?.username,
                    it.user?.profile_photo_path
                )
            }
            catDatabase.messageRoomDao().deleteAllMessageRoom()
            catDatabase.messageRoomDao().insertMessageRoom(newRoomMessage!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<MessageRoomItems>>> =
            catDatabase.messageRoomDao().getMessageRoom(sender_user_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }
}
package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.MessageRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.MessageItems
import com.catpedigree.capstone.catpedigreebase.utils.Result

class MessageRepository(
    private val messageRemoteDataSource: MessageRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    fun getMessage(token: String, room_id: Int): LiveData<Result<List<MessageItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = messageRemoteDataSource.getMessage(token, room_id)
            val messages = response.body()?.data
            val newMessage = messages?.map {
                MessageItems(
                    it.id,
                    it.room_id,
                    it.user_id,
                    it.messages,
                    it.user?.name,
                    it.user?.username,
                    it.user?.profile_photo_path
                )
            }
            catDatabase.messageDao().deleteAllMessage()
            catDatabase.messageDao().insertMessage(newMessage!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<MessageItems>>> =
            catDatabase.messageDao().getMessage(room_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }
}
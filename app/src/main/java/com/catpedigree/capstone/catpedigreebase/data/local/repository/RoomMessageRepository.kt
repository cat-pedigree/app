package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.RoomMessageRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.RoomMessageItems
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.FollowError
import com.catpedigree.capstone.catpedigreebase.utils.error.RoomError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomMessageRepository(
    private val messageRemoteDataSource: RoomMessageRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    fun getMessageRoom(token: String, sender_user_id: Int): LiveData<Result<List<RoomMessageItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = messageRemoteDataSource.getMessageRoom(token, sender_user_id)
            val rooms = response.body()?.data
            val newRoomMessage = rooms?.map {
                RoomMessageItems(
                    it.id,
                    it.receiver_user_id,
                    it.sender_user_id,
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
        val dataLocal: LiveData<Result<List<RoomMessageItems>>> =
            catDatabase.messageRoomDao().getMessageRoom(sender_user_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    suspend fun postRoom(
        token: String,
        receiver_user_id: Int,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    messageRemoteDataSource.postRoom(
                        token,
                        receiver_user_id
                    )

                if (!response.isSuccessful) {
                    throw RoomError(response.message())
                }
            } catch (e: Throwable) {
                throw RoomError(e.message.toString())
            }
        }
    }
}
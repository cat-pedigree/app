package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.RoomMessageInterface

class RoomMessageRemoteDataSource(private val messageInterface: RoomMessageInterface) {
    suspend fun getMessageRoom(
        token: String,
        sender_user_id: Int
    ) = messageInterface.getMessageRoom("Bearer $token", sender_user_id)

    suspend fun postRoom(
        token: String,
        receiver_user_id: Int,
    ) = messageInterface.postRoom("Bearer $token", receiver_user_id)
}
package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.MessageInterface

class MessageRemoteDataSource(private val messageInterface: MessageInterface) {
    suspend fun getMessageRoom(
        token: String,
        sender_user_id: Int
    ) = messageInterface.getMessageRoom("Bearer $token", sender_user_id)
}
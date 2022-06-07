package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.MessageInterface

class MessageRemoteDataSource(private val messageInterface: MessageInterface) {
    suspend fun getMessage(
        token: String,
        room_id: Int
    ) = messageInterface.getMessage("Bearer $token", room_id)
}
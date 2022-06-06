package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.MessageRoomResponse
import retrofit2.Response
import retrofit2.http.*

interface MessageInterface {

    @GET("messages/room")
    suspend fun getMessageRoom(
        @Header("Authorization") token: String,
        @Query("sender_user_id") sender_user_id: Int
    ): Response<MessageRoomResponse>
}
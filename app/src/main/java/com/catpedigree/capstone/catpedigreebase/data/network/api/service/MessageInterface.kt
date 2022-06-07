package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.MessageResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.RoomCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.RoomMessageResponse
import retrofit2.Response
import retrofit2.http.*

interface MessageInterface {

    @GET("message/all")
    suspend fun getMessage(
        @Header("Authorization") token: String,
        @Query("room_id") room_id: Int
    ): Response<MessageResponse>
}
package com.catpedigree.capstone.catpedigreebase.data.network.api.service
import com.catpedigree.capstone.catpedigreebase.data.network.response.RoomCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.RoomMessageResponse
import retrofit2.Response
import retrofit2.http.*

interface RoomMessageInterface {

    @GET("room/all")
    suspend fun getMessageRoom(
        @Header("Authorization") token: String,
        @Query("sender_user_id") sender_user_id: Int
    ): Response<RoomMessageResponse>

    @FormUrlEncoded
    @POST("room/create")
    suspend fun postRoom(
        @Header("Authorization") token: String,
        @Field("receiver_user_id") user_id: Int,
    ): Response<RoomCreateResponse>
}
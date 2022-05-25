package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.CommentCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.CommentResponse
import retrofit2.Response
import retrofit2.http.*

interface CommentInterface {
    @FormUrlEncoded
    @POST("comment/create")
    suspend fun postComment(
        @Header("Authorization") token:String,
        @Field("post_id") post_id: Int,
        @Field("user_id") user_id: Int,
        @Field("description") description: String
    ): Response<CommentCreateResponse>

    @GET("comment/all")
    suspend fun getComment(
        @Header("Authorization") token: String,
        @Query("post_id") post_id: Int,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): Response<CommentResponse>
}
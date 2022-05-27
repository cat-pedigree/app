package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.LoveCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.LoveDeleteResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.PostCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PostInterface {
    @GET("post/all")
    suspend fun getPost(
        @Header("Authorization") token: String,
    ): Response<PostResponse>

    @Multipart
    @POST("post/create")
    suspend fun postCreate(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("title") title: String,
        @Part("description") description: RequestBody,
    ): Response<PostCreateResponse>

    @FormUrlEncoded
    @POST("love/create")
    suspend fun loveCreate(
        @Header("Authorization") token:String,
        @Field("post_id") post_id: Int,
        @Field("user_id") user_id: Int
    ): Response<LoveCreateResponse>

    @DELETE("love/delete")
    suspend fun loveDelete(
        @Header("Authorization") token: String,
        @Query("post_id") post_id: Int,
        @Query("user_id") user_id: Int
    ): Response<LoveDeleteResponse>
}
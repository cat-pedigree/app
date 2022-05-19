package com.catpedigree.capstone.catpedigreebase.api.apiinterface

import com.catpedigree.capstone.catpedigreebase.data.response.LoveCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.response.PostResponse
import retrofit2.Response
import retrofit2.http.*

interface PostInterface {
    @GET("post/all")
    suspend fun getPost(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 10,
    ): Response<PostResponse>

    @FormUrlEncoded
    @POST("love/create")
    suspend fun postLove(
        @Header("Authorization") token:String,
        @Field("post_id") post_id: Int,
        @Field("user_id") user_id: Int
    ): Response<LoveCreateResponse>
}
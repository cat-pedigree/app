package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.CatCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.CommentCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.CommentResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.PostCreateResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CatInterface {
    @Multipart
    @POST("cat/create")
    suspend fun catCreate(
        @Header("Authorization") token: String,
        @Part("user_id") user_id: Int,
        @Part("name") name: String,
        @Part("breed") breed: String,
        @Part("gender") gender: String,
        @Part("color") color: String,
        @Part("weight") weight: Double,
        @Part("age") age: Int,
        @Part("story") story: String,
        @Part photo: MultipartBody.Part,
    ): Response<CatCreateResponse>
}
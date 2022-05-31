package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.*
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
        @Part("name") name: RequestBody,
        @Part("breed") breed: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("color") color: RequestBody,
        @Part("weight") weight: Double,
        @Part("age") age: Int,
        @Part("story") story: RequestBody,
        @Part photo: MultipartBody.Part,
    ): Response<CatCreateResponse>

    @GET("cat/all")
    suspend fun getCat(
        @Header("Authorization") token: String,
        @Query("user_id") user_id: Int
    ): Response<CatResponse>

    @Multipart
    @POST("cat/album")
    suspend fun catCreateAlbum(
        @Header("Authorization") token: String,
        @Part("user_id") user_id: Int,
        @Part("cat_id") cat_id: Int,
        @Part photo: MultipartBody.Part,
    ): Response<CatCreateResponse>

    @GET("cat/album")
    suspend fun getCatAlbum(
        @Header("Authorization") token: String,
        @Query("user_id") user_id: Int,
        @Query("cat_id") cat_id:Int
    ): Response<AlbumResponse>
}
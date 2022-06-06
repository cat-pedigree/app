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
        @Part("eye_color") eye_color: RequestBody,
        @Part("hair_color") hair_color: RequestBody,
        @Part("ear_shape") ear_shape: RequestBody,
        @Part("weight") weight: Double,
        @Part("age") age: Int,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: Double? = null,
        @Part("lon") lon: Double? = null,
    ): Response<CatCreateResponse>

    @GET("cat/all")
    suspend fun getCat(
        @Header("Authorization") token: String,
        @Query("user_id") user_id: Int
    ): Response<CatResponse>

    @GET("cat/search")
    suspend fun getCatFilter(
        @Header("Authorization") token: String,
        @Query("breed") breed: String?= null,
        @Query("color") color: String?= null,
        @Query("eye_color") eye_color: String?= null,
        @Query("hair_color") hair_color: String?= null,
        @Query("ear_shape") ear_shape: String?= null,
    ): Response<CatResponse>

    @GET("cat/all")
    suspend fun getCatLocation(
        @Header("Authorization") token: String
    ): Response<CatResponse>
}
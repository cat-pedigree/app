package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.RegisterResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.UserChangeResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.UserLoginResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.UserResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserInterface {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserLoginResponse>

    @FormUrlEncoded
    @POST("user")
    suspend fun profile(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("username") username: String,
        @Field("bio") bio: String,
    ): Response<UserChangeResponse>

    @Multipart
    @POST("change")
    suspend fun change(
        @Header("Authorization") token: String,
        @Part profile_photo_path: MultipartBody.Part,
    ): Response<UserChangeResponse>

    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): Response<UserResponse>

    @GET("user")
    suspend fun search(
        @Header("Authorization") token: String,
        @Query("name") name: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("user/email")
    suspend fun changeEmail(
        @Header("Authorization") token: String,
        @Field("email") email: String,
    ): Response<UserChangeResponse>

    @FormUrlEncoded
    @POST("user/password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Field("password") password: String,
    ): Response<UserChangeResponse>

    @DELETE("user/delete")
    suspend fun userDelete(
        @Header("Authorization") token: String,
        @Query("id") id: Int
    ): Response<UserChangeResponse>
}
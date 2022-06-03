package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.FollowCreateResponse
import com.catpedigree.capstone.catpedigreebase.data.network.response.FollowResponse
import retrofit2.Response
import retrofit2.http.*

interface FollowInterface {
    @FormUrlEncoded
    @POST("follow/create")
    suspend fun postFollow(
        @Header("Authorization") token: String,
        @Field("user_id") user_id: Int,
        @Field("follower_id") follower_id: Int
    ): Response<FollowCreateResponse>

    @GET("follow/follower")
    suspend fun getFollow(
        @Header("Authorization") token: String,
        @Query("user_id") user_id: Int,
    ): Response<FollowResponse>

    @GET("follow/following")
    suspend fun getFollowing(
        @Header("Authorization") token: String,
        @Query("follower_id") follower_id: Int,
    ): Response<FollowResponse>

    @DELETE("follow/delete")
    suspend fun followDelete(
        @Header("Authorization") token: String,
        @Query("user_id") user_id: Int,
        @Query("follower_id") follower_id: Int,
    ): Response<FollowCreateResponse>
}
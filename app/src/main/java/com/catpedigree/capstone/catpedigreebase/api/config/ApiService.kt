package com.catpedigree.capstone.catpedigreebase.api.config

import com.catpedigree.capstone.catpedigreebase.data.response.CommentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("comment/all")
    suspend fun getComment(
        @Header("Authorization") token: String,
        @Query("post_id") post_id: Int,
    ): Response<CommentResponse>
}
package com.catpedigree.capstone.catpedigreebase.data.remote

import com.catpedigree.capstone.catpedigreebase.api.apiinterface.PostInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRemoteDataSource(private val postInterface: PostInterface) {

    suspend fun getPost(token: String, page: Int, size: Int) =
        postInterface.getPost("Bearer $token", page, size)

    suspend fun postCreate(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ) = postInterface.postCreate("Bearer $token", photo, description, lat, lon)

    suspend fun loveCreate(
        token: String,
        post_id: Int,
        user_id: Int
    ) = postInterface.loveCreate("Bearer $token", post_id, user_id)

    suspend fun loveDelete(
        token: String,
        post_id: Int,
        user_id: Int
    ) = postInterface.loveDelete("Bearer $token", post_id,user_id)
}
package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.PostInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRemoteDataSource(private val postInterface: PostInterface) {

    suspend fun getPost(token: String) =
        postInterface.getPost("Bearer $token")

    suspend fun getPostProfile(token: String, user_id: Int) =
        postInterface.getPostProfile("Bearer $token", user_id)

    suspend fun postCreate(
        token: String,
        photo: MultipartBody.Part,
        title: String,
        description: RequestBody,
    ) = postInterface.postCreate("Bearer $token", photo, title, description)

    suspend fun loveCreate(
        token: String,
        post_id: Int,
        user_id: Int
    ) = postInterface.loveCreate("Bearer $token", post_id, user_id)

    suspend fun loveDelete(
        token: String,
        post_id: Int,
        user_id: Int
    ) = postInterface.loveDelete("Bearer $token", post_id, user_id)
}
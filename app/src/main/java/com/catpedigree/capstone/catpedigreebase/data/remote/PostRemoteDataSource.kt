package com.catpedigree.capstone.catpedigreebase.data.remote

import com.catpedigree.capstone.catpedigreebase.api.apiinterface.PostInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRemoteDataSource(private val postInterface: PostInterface) {

    suspend fun getPost(token: String, page: Int, size: Int) =
        postInterface.getPost("Bearer $token", page, size)
//
//    suspend fun getStoriesWithLocation(token: String, page: Int, size: Int) =
//        storyInterface.getStories("Bearer $token", page, size, 1)
//
    suspend fun postCreate(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ) = postInterface.postCreate("Bearer $token", photo, description, lat, lon)
}
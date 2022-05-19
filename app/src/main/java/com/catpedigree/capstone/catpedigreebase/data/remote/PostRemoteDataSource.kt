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
//    suspend fun postStories(
//        token: String,
//        file: MultipartBody.Part,
//        description: RequestBody,
//        lat: Double? = null,
//        lon: Double? = null
//    ) = storyInterface.postStory("Bearer $token", file, description, lat, lon)

    suspend fun postLove(
        token: String,
        post_id: Int,
        user_id: Int
    ) = postInterface.postLove("Bearer $token", post_id, user_id)
}
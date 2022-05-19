package com.catpedigree.capstone.catpedigreebase.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.catpedigree.capstone.catpedigreebase.data.database.PostDatabase
import com.catpedigree.capstone.catpedigreebase.data.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.remote.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.remote.PostRemoteMediator

class PostRepository(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val postDatabase: PostDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPosts(token: String): LiveData<PagingData<PostItems>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = PostRemoteMediator(token, postRemoteDataSource, postDatabase),
            pagingSourceFactory = {
                postDatabase.postDao().getPosts()
            }).liveData
    }
//
//    suspend fun getStoriesWithLocation(token: String): List<StoryItems> {
//        val stories = mutableListOf<StoryItems>()
//        withContext(Dispatchers.IO) {
//            try {
//                val response = storyRemoteDataSource.getStoriesWithLocation(token, 1, 20)
//                if (response.isSuccessful) {
//                    response.body()?.listStory?.forEach {
//                        stories.add(
//                            StoryItems(
//                                id = it.id,
//                                name = it.name,
//                                createdAt = it.createdAt,
//                                imageUrl = it.photoUrl,
//                                description = it.description,
//                                lat = it.lat,
//                                lon = it.lon
//                            )
//                        )
//                    }
//                }
//            } catch (e: Throwable) {
//                throw StoryError(e.message.toString())
//            }
//        }
//
//        return stories
//    }
//
//    suspend fun postStory(
//        token: String,
//        file: MultipartBody.Part,
//        description: RequestBody,
//        latLng: LatLng?
//    ) {
//        withContext(Dispatchers.IO) {
//            try {
//                val response = if (latLng != null) {
//                    storyRemoteDataSource.postStories(
//                        token,
//                        file,
//                        description,
//                        latLng.latitude,
//                        latLng.longitude
//                    )
//                } else {
//                    storyRemoteDataSource.postStories(token, file, description)
//                }
//
//                if (!response.isSuccessful) {
//                    throw StoryError(response.message())
//                }
//            } catch (e: Throwable) {
//                throw StoryError(e.message.toString())
//            }
//        }
//    }
}
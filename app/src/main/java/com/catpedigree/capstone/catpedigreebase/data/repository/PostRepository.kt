package com.catpedigree.capstone.catpedigreebase.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.catpedigree.capstone.catpedigreebase.data.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.remote.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.remote.PostRemoteMediator
import com.catpedigree.capstone.catpedigreebase.utils.PostError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRepository(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPosts(token: String): LiveData<PagingData<PostItems>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = PostRemoteMediator(token, postRemoteDataSource, catDatabase),
            pagingSourceFactory = {
                catDatabase.postDao().getPosts()
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
    suspend fun postCreate(
    token: String,
    file: MultipartBody.Part,
    description: RequestBody,
    latLng: LatLng?
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response = if (latLng != null) {
                    postRemoteDataSource.postCreate(
                        token,
                        file,
                        description,
                        latLng.latitude,
                        latLng.longitude
                    )
                } else {
                    postRemoteDataSource.postCreate(token, file, description)
                }

                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }
}
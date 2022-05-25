package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.remote.mediator.PostRemoteMediator
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
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

    suspend fun loveCreate(
        token: String,
        post_id: Int,
        user_id: Int,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    postRemoteDataSource.loveCreate(
                        token,
                        post_id,
                        user_id
                    )

                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }

    suspend fun loveDelete(
        token: String,
        post_id: Int,
        user_id: Int,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    postRemoteDataSource.loveDelete(
                        token,
                        post_id,
                        user_id
                    )

                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }
}
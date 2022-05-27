package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class PostRepository(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val catDatabase: CatDatabase
) {

//    @OptIn(ExperimentalPagingApi::class)
    fun getPosts(token: String): LiveData<Result<List<PostItems>>> = liveData {
        emit(Result.Loading)
        try{
            val response = postRemoteDataSource.getPost(token)
            val posts = response.body()?.data
            val newPost = posts?.map { post ->
                val isBookmarked = catDatabase.postDao().isPostsBookmarked(post.id!!)
                val isLoved = catDatabase.postDao().isPostsLoved(post.id)
                PostItems(
                    post.id,
                    post.user?.name,
                    post.user?.profile_photo_path,
                    post.photo,
                    post.title,
                    post.description,
                    post.created_at,
                    post.loves_count,
                    post.comments_count,
                    isBookmarked,
                    isLoved
                )
            }
            catDatabase.postDao().deleteAllPosts()
            catDatabase.postDao().insertPost(newPost!!)
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<PostItems>>> = catDatabase.postDao().getPosts().map { Result.Success(it) }
        emitSource(dataLocal)
//        return Pager(
//            config = PagingConfig(pageSize = 5),
//            remoteMediator = PostRemoteMediator(token, postRemoteDataSource, catDatabase),
//            pagingSourceFactory = {
//                catDatabase.postDao().getPosts()
//            }).liveData
    }

    suspend fun postCreate(
    token: String,
    file: MultipartBody.Part,
    title: String,
    description: RequestBody,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    postRemoteDataSource.postCreate(
                        token,
                        file,
                        title,
                        description,
                    )

                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }

    suspend fun setPostBookmark(post: PostItems, bookmarkState: Boolean){
        post.isBookmarked = bookmarkState
        catDatabase.postDao().updatePost(post)
    }

    suspend fun setPostLove(post: PostItems, loveState: Boolean){
        post.isLoved = loveState
        catDatabase.postDao().updatePost(post)
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
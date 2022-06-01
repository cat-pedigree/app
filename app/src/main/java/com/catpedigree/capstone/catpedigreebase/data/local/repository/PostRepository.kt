package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRepository(
    private val postRemoteDataSource: PostRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    fun getPosts(token: String): LiveData<Result<List<PostItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = postRemoteDataSource.getPost(token)
            val posts = response.body()?.data
            val newPost = posts?.map { post ->
                val isBookmarked = catDatabase.postDao().isPostsBookmarked(post.id!!)
                val isLoved = catDatabase.postDao().isPostsLoved(post.id)
                PostItems(
                    post.id,
                    post.photo,
                    post.title,
                    post.description,
                    post.created_at,
                    post.loves_count,
                    post.comments_count,
                    isBookmarked,
                    isLoved,
                    post.user?.id,
                    post.user?.name,
                    post.user?.profile_photo_path,
                )
            }
            catDatabase.postDao().deleteAllPosts()
            catDatabase.postDao().insertPost(newPost!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<PostItems>>> =
            catDatabase.postDao().getPosts().map { Result.Success(it) }
        emitSource(dataLocal)
    }

    fun getPostsProfile(token: String, user_id: Int): LiveData<Result<List<PostItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = postRemoteDataSource.getPostProfile(token, user_id)
            val posts = response.body()?.data
            val newPost = posts?.map { post ->
                val isBookmarked = catDatabase.postDao().isPostsBookmarked(post.id!!)
                val isLoved = catDatabase.postDao().isPostsLoved(post.id)
                PostItems(
                    post.id,
                    post.photo,
                    post.title,
                    post.description,
                    post.created_at,
                    post.loves_count,
                    post.comments_count,
                    isBookmarked,
                    isLoved,
                    post.user?.id,
                    post.user?.name,
                    post.user?.profile_photo_path,
                )
            }
            catDatabase.postDao().deleteAllPostsUser(user_id)
            catDatabase.postDao().insertPost(newPost!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<PostItems>>> =
            catDatabase.postDao().getPostProfile(user_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    fun getPostFavorite(): LiveData<List<PostItems>> {
        return catDatabase.postDao().getPostFavorite()
    }

    fun getPostFavoriteCount(): LiveData<Int> {
        return catDatabase.postDao().getPostFavoriteCount()
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

    suspend fun setPostBookmark(post: PostItems, bookmarkState: Boolean) {
        post.isBookmarked = bookmarkState
        catDatabase.postDao().updatePost(post)
    }

    suspend fun setPostLove(post: PostItems, loveState: Boolean) {
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
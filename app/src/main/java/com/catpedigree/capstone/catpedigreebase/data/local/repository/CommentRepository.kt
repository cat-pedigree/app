package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.CommentRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.CommentItems
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.CommentError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentRepository(
    private val commentRemoteDataSource: CommentRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    fun getComments(token: String, post_id: Int): LiveData<Result<List<CommentItems>>> = liveData {
        emit(Result.Loading)
        try{
            val response = commentRemoteDataSource.getComment(token,post_id)
            val comments = response.body()?.data
            val newComment = comments?.map { comment ->
                CommentItems(
                    comment.id,
                    comment.post_id,
                    comment.user_id,
                    comment.description,
                    comment.user?.name,
                    comment.user?.profile_photo_path
                )
            }
            catDatabase.commentDao().deleteAllComments()
            catDatabase.commentDao().insertComment(newComment!!)
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<CommentItems>>> = catDatabase.commentDao().getComments(post_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    suspend fun commentCreate(
        token: String,
        post_id: Int,
        user_id: Int,
        description: String,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    commentRemoteDataSource.postComment(
                        token,
                        post_id,
                        user_id,
                        description
                    )

                if (!response.isSuccessful) {
                    throw CommentError(response.message())
                }
            } catch (e: Throwable) {
                throw CommentError(e.message.toString())
            }
        }
    }
}
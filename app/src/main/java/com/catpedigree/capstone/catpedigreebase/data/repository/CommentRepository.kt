package com.catpedigree.capstone.catpedigreebase.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.catpedigree.capstone.catpedigreebase.data.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.item.CommentItems
import com.catpedigree.capstone.catpedigreebase.data.remote.CommentRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.remote.CommentRemoteMediator
import com.catpedigree.capstone.catpedigreebase.utils.CommentError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentRepository(
    private val commentRemoteDataSource: CommentRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getComments(token: String, post_id: Int): LiveData<PagingData<CommentItems>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            remoteMediator = CommentRemoteMediator(token,post_id, commentRemoteDataSource, catDatabase),
            pagingSourceFactory = {
                catDatabase.commentDao().getComments(post_id)
            }).liveData
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
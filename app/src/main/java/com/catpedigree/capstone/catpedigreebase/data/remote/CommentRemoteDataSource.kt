package com.catpedigree.capstone.catpedigreebase.data.remote

import com.catpedigree.capstone.catpedigreebase.api.apiinterface.CommentInterface

class CommentRemoteDataSource(private val commentInterface: CommentInterface) {

    suspend fun getComment(token: String,post_id: Int, page: Int, size: Int) =
        commentInterface.getComment("Bearer $token",post_id, page, size)

    suspend fun postComment(
        token: String,
        post_id: Int,
        user_id: Int,
        description: String
    ) = commentInterface.postComment("Bearer $token", post_id, user_id, description)
}
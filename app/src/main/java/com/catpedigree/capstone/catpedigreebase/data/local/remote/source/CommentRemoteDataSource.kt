package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.CommentInterface

class CommentRemoteDataSource(private val commentInterface: CommentInterface) {

    suspend fun getComment(token: String,post_id: Int) =
        commentInterface.getComment("Bearer $token",post_id)

    suspend fun postComment(
        token: String,
        post_id: Int,
        user_id: Int,
        description: String
    ) = commentInterface.postComment("Bearer $token", post_id, user_id, description)
}
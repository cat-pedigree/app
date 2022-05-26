package com.catpedigree.capstone.catpedigreebase.data.network.response

import com.google.gson.annotations.SerializedName

data class  CommentResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<CommentData>? = null,
)

data class CommentData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("post_id")
    val post_id: Int? = null,

    @field:SerializedName("user_id")
    val user_id: Int? = null,

    @field:SerializedName("user")
    val user: UserPostData? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,
)

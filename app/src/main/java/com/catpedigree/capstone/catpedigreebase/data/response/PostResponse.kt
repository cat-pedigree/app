package com.catpedigree.capstone.catpedigreebase.data.response

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<PostData>? = null,
)

data class PostData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("user")
    val user: UserPostData? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lat")
    val lat: Double? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("loves_count")
    val loves_count: Int? = null,

    @field:SerializedName("comments_count")
    val comments_count: Int? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null
)

data class UserPostData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,
)
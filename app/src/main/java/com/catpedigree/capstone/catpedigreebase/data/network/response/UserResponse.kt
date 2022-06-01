package com.catpedigree.capstone.catpedigreebase.data.network.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<DataUser>? = null,
)

data class DataUser(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,

    @field: SerializedName("posts_count")
    val posts_count: Int? = null,

    @field: SerializedName("cats_count")
    val cats_count: Int? = null,
)

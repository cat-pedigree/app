package com.catpedigree.capstone.catpedigreebase.data.network.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<MessageData>? = null,
)

data class MessageData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("room_id")
    val room_id: Int? = null,

    @field:SerializedName("user_id")
    val user_id: Int? = null,

    @field:SerializedName("messages")
    val messages: String? = null,

    @field:SerializedName("user")
    val user: UserMessageData? = null,
)

data class UserMessageData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,
)
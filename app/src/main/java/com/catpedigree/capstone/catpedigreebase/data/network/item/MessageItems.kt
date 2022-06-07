package com.catpedigree.capstone.catpedigreebase.data.network.item

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "message_items")
data class MessageItems(

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("room_id")
    val room_id: Int? = null,

    @field:SerializedName("user_id")
    val user_id: Int? = null,

    @field:SerializedName("messages")
    val messages: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null
) : Parcelable
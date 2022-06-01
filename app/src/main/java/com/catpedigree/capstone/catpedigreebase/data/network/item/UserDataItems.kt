package com.catpedigree.capstone.catpedigreebase.data.network.item

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_items")
data class UserDataItems(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("profile_path")
    val profile_photo_path: String? = null,

    @field:SerializedName("postsCount")
    val postsCount: Int? = null,

    @field:SerializedName("catsCount")
    val catsCount: Int? = null,
): Parcelable

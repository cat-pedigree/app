package com.catpedigree.capstone.catpedigreebase.data.network.item

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "comment_items")
data class CommentItems(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("post_id")
    val post_id: Int? = null,

    @field:SerializedName("user_id")
    val user_id: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,
): Parcelable
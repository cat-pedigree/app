package com.catpedigree.capstone.catpedigreebase.data.network.item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_comment_items")
data class RemoteCommentItems(
    @PrimaryKey val id: Int?,
    val prevKey: Int?,
    val nextKey: Int?
)
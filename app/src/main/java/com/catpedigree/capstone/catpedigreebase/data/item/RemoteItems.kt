package com.catpedigree.capstone.catpedigreebase.data.item

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_items")
data class RemoteItems(
    @PrimaryKey val id: Int?,
    val prevKey: Int?,
    val nextKey: Int?
)

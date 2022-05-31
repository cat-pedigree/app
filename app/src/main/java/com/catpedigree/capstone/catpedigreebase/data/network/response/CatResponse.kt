package com.catpedigree.capstone.catpedigreebase.data.network.response

import com.google.gson.annotations.SerializedName

data class CatResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<CatData>? = null,
)

data class CatData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("user_id")
    val user_id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("breed")
    val breed: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("color")
    val color: String? = null,

    @field:SerializedName("weight")
    val weight: Double? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("story")
    val story: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,
)
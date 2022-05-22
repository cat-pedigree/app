package com.catpedigree.capstone.catpedigreebase.data.response

import com.google.gson.annotations.SerializedName

data class PostCreateResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null
)
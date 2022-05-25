package com.catpedigree.capstone.catpedigreebase.data.network.api.config

import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.data.network.api.service.CommentInterface
import com.catpedigree.capstone.catpedigreebase.data.network.api.service.PostInterface
import com.catpedigree.capstone.catpedigreebase.data.network.api.service.UserInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    var BASE_URL = BuildConfig.BASE_API_URL

    private val loggingInterceptor = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getUserApi(): UserInterface {
        return retrofit.create(UserInterface::class.java)
    }

    fun getPostApi(): PostInterface {
        return retrofit.create(PostInterface::class.java)
    }

    fun getCommentApi(): CommentInterface{
        return retrofit.create(CommentInterface::class.java)
    }
}
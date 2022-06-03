package com.catpedigree.capstone.catpedigreebase.data.network.api.config

import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.data.network.api.service.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiConfig {

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
            .baseUrl(BuildConfig.BASE_API_URL)
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

    fun getCommentApi(): CommentInterface {
        return retrofit.create(CommentInterface::class.java)
    }

    fun getCatApi(): CatInterface {
        return retrofit.create(CatInterface::class.java)
    }

    fun getVeterinary(): VeterinaryInterface {
        return retrofit.create(VeterinaryInterface::class.java)
    }

    fun getFollow(): FollowInterface {
        return retrofit.create(FollowInterface::class.java)
    }
}
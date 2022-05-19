package com.catpedigree.capstone.catpedigreebase.api.config

import androidx.viewbinding.BuildConfig
import com.catpedigree.capstone.catpedigreebase.api.apiinterface.PostInterface
import com.catpedigree.capstone.catpedigreebase.api.apiinterface.UserInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
            .baseUrl("http://192.168.1.4/api-cat/public/api/")
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
}
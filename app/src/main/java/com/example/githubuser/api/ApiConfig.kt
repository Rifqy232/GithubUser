package com.example.githubuser.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        fun getApiService(): ApiService {
            val interceptor = Interceptor {chain ->
                val req = chain.request()
                val requestHeader =  req.newBuilder()
                    .addHeader("Authorization", "token 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")
                    .build()
                chain.proceed(requestHeader)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
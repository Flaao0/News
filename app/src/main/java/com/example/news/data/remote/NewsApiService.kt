package com.example.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything?apiKey=0e0e421655db4f96bc099826019d5022")
    suspend fun loadArticles(
        @Query("q") topic: String,
    ): NewsResponseDto
}
package com.example.news.domain.repository

import com.example.news.domain.entity.Article
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun getAllFavorites(): Flow<List<Article>>

    fun isFavorite(url: String): Flow<Boolean>

    suspend fun addFavorite(article: Article)

    suspend fun removeFavorite(url: String)
}

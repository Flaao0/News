package com.example.news.data.repository

import com.example.news.data.local.NewsDao
import com.example.news.data.mapper.toFavoriteDbModel
import com.example.news.data.mapper.toFavoriteEntities
import com.example.news.domain.entity.Article
import com.example.news.domain.repository.FavoritesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
) : FavoritesRepository {

    override fun getAllFavorites(): Flow<List<Article>> {
        return newsDao.getAllFavorites().map { it.toFavoriteEntities() }
    }

    override fun isFavorite(url: String): Flow<Boolean> {
        return newsDao.getFavoriteByUrl(url).map { it != null }
    }

    override suspend fun addFavorite(article: Article) {
        newsDao.insertFavorite(article.toFavoriteDbModel())
    }

    override suspend fun removeFavorite(url: String) {
        newsDao.deleteFavorite(url)
    }
}

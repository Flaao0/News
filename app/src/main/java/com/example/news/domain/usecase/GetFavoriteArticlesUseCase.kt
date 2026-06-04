package com.example.news.domain.usecase

import com.example.news.domain.entity.Article
import com.example.news.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteArticlesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {

    operator fun invoke(): Flow<List<Article>> {
        return favoritesRepository.getAllFavorites()
    }
}

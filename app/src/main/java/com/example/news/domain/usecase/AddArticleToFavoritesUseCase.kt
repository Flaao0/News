package com.example.news.domain.usecase

import com.example.news.domain.entity.Article
import com.example.news.domain.repository.FavoritesRepository
import javax.inject.Inject

class AddArticleToFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {

    suspend operator fun invoke(article: Article) {
        favoritesRepository.addFavorite(article)
    }
}

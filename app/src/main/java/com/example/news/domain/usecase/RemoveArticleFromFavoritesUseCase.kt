package com.example.news.domain.usecase

import com.example.news.domain.repository.FavoritesRepository
import javax.inject.Inject

class RemoveArticleFromFavoritesUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {

    suspend operator fun invoke(url: String) {
        favoritesRepository.removeFavorite(url)
    }
}

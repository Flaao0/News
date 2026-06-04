package com.example.news.domain.usecase

import com.example.news.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveIsFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
) {

    operator fun invoke(url: String): Flow<Boolean> {
        return favoritesRepository.isFavorite(url)
    }
}

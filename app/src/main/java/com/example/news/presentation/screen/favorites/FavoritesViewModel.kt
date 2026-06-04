package com.example.news.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.entity.Article
import com.example.news.domain.usecase.GetFavoriteArticlesUseCase
import com.example.news.domain.usecase.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteArticlesUseCase: GetFavoriteArticlesUseCase,
    private val removeArticleFromFavoritesUseCase: RemoveArticleFromFavoritesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()

    init {
        getFavoriteArticlesUseCase()
            .onEach { articles ->
                _state.update { it.copy(articles = articles) }
            }
            .launchIn(viewModelScope)
    }

    fun processCommand(command: FavoritesCommand) {
        when (command) {
            is FavoritesCommand.Remove -> {
                viewModelScope.launch {
                    removeArticleFromFavoritesUseCase(command.url)
                }
            }
        }
    }
}

data class FavoritesState(
    val articles: List<Article> = emptyList(),
)

sealed interface FavoritesCommand {
    data class Remove(val url: String) : FavoritesCommand
}

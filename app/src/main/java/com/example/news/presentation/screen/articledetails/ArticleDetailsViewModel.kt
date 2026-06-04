package com.example.news.presentation.screen.articledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.entity.Article
import com.example.news.domain.usecase.AddArticleToFavoritesUseCase
import com.example.news.domain.usecase.GetArticleByUrlUseCase
import com.example.news.domain.usecase.ObserveIsFavoriteUseCase
import com.example.news.domain.usecase.RemoveArticleFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getArticleByUrlUseCase: GetArticleByUrlUseCase,
    observeIsFavoriteUseCase: ObserveIsFavoriteUseCase,
    private val addArticleToFavoritesUseCase: AddArticleToFavoritesUseCase,
    private val removeArticleFromFavoritesUseCase: RemoveArticleFromFavoritesUseCase,
) : ViewModel() {

    private val articleUrl: String = savedStateHandle.get<String>("articleUrl").orEmpty()

    private val _state = MutableStateFlow<ArticleDetailsState>(ArticleDetailsState.Loading)
    val state = _state.asStateFlow()

    init {
        if (articleUrl.isEmpty()) {
            _state.update { ArticleDetailsState.NotFound }
        } else {
            combine(
                getArticleByUrlUseCase(articleUrl),
                observeIsFavoriteUseCase(articleUrl),
            ) { article, isFavorite ->
                if (article != null) {
                    ArticleDetailsState.Content(article = article, isFavorite = isFavorite)
                } else {
                    ArticleDetailsState.NotFound
                }
            }
                .onEach { newState -> _state.update { newState } }
                .launchIn(viewModelScope)
        }
    }

    fun processCommand(command: ArticleDetailsCommand) {
        when (command) {
            ArticleDetailsCommand.ToggleFavorite -> {
                val content = state.value as? ArticleDetailsState.Content ?: return
                viewModelScope.launch {
                    if (content.isFavorite) {
                        removeArticleFromFavoritesUseCase(content.article.url)
                    } else {
                        addArticleToFavoritesUseCase(content.article)
                    }
                }
            }
        }
    }
}

sealed interface ArticleDetailsState {
    data object Loading : ArticleDetailsState
    data class Content(val article: Article, val isFavorite: Boolean) : ArticleDetailsState
    data object NotFound : ArticleDetailsState
}

sealed interface ArticleDetailsCommand {
    data object ToggleFavorite : ArticleDetailsCommand
}

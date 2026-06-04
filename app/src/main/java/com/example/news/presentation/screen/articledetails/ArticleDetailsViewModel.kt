package com.example.news.presentation.screen.articledetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.entity.Article
import com.example.news.domain.usecase.GetArticleByUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ArticleDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getArticleByUrlUseCase: GetArticleByUrlUseCase,
) : ViewModel() {

    private val articleUrl: String = savedStateHandle.get<String>("articleUrl").orEmpty()

    private val _state = MutableStateFlow<ArticleDetailsState>(ArticleDetailsState.Loading)
    val state = _state.asStateFlow()

    init {
        if (articleUrl.isEmpty()) {
            _state.update { ArticleDetailsState.NotFound }
        } else {
            getArticleByUrlUseCase(articleUrl)
                .onEach { article ->
                    _state.update {
                        if (article != null) {
                            ArticleDetailsState.Content(article)
                        } else {
                            ArticleDetailsState.NotFound
                        }
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}

sealed interface ArticleDetailsState {
    data object Loading : ArticleDetailsState
    data class Content(val article: Article) : ArticleDetailsState
    data object NotFound : ArticleDetailsState
}

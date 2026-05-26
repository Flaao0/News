package com.example.news.domain.usecase

import com.example.news.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext


class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic = topic)
        CoroutineScope(coroutineContext).launch {
            newsRepository.updateArticlesForTopic(topic)
        }

    }
}
package com.example.news.domain.usecase

import com.example.news.domain.repository.NewsRepository
import javax.inject.Inject


class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic = topic)
        newsRepository.updateArticlesForTopic(topic)
    }
}
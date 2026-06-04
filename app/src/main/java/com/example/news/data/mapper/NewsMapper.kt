package com.example.news.data.mapper

import com.example.news.data.local.ArticleDbModel
import com.example.news.data.remote.NewsResponseDto
import com.example.news.domain.entity.Article
import com.example.news.domain.entity.Interval
import com.example.news.domain.entity.Language
import com.example.news.domain.entity.RefreshConfig
import com.example.news.domain.entity.Settings
import java.text.SimpleDateFormat
import java.util.Locale

fun NewsResponseDto.toDbModels(topic: String): List<ArticleDbModel>  {
    return articleDto.map {
        ArticleDbModel(
            title = it.title,
            description = it.description,
            url = it.url,
            imageUrl = it.urlToImage,
            sourceName = it.sourceDto.name,
            topic = topic,
            publishedAt = it.publishedAt.toTimeStamp()
        )
    }
}

fun ArticleDbModel.toEntity(): Article {
    return Article(
        title = title,
        description = description,
        imageUrl = imageUrl,
        sourceName = sourceName,
        publishedAt = publishedAt,
        url = url
    )
}

fun List<ArticleDbModel>.toEntities(): List<Article> {
    return map { it.toEntity() }.distinct()
}

private fun String.toTimeStamp() : Long {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return dateFormatter.parse(this)?.time ?: System.currentTimeMillis()
}

fun Int.toInterval(): Interval {
    return Interval.entries.first { it.minutes == this }
}

fun Settings.toRefreshConfig(): RefreshConfig {
    return RefreshConfig(
        language, interval, wifiOnly
    )
}

fun Language.toQueryParam(): String {
    return when(this) {
        Language.ENGLISH -> "en"
        Language.RUSSIAN -> "ru"
        Language.FRENCH -> "fr"
        Language.GERMAN -> "ge"
    }
}
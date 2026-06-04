package com.example.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ArticleDbModel::class,
        SubscriptionDbModel::class,
        FavoriteArticleDbModel::class,
    ],
    version = 3,
    exportSchema = false
)
abstract class NewsDatabase: RoomDatabase() {

    abstract fun newsDao(): NewsDao
}
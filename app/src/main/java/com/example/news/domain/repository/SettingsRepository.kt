package com.example.news.domain.repository

import com.example.news.domain.entity.Interval
import com.example.news.domain.entity.Language
import com.example.news.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateLanguage(language: Language)

    suspend fun updateInterval(interval: Interval)

    suspend fun updateNotificationEnabled(enabled: Boolean)

    suspend fun updateWifiOnly(wifiOnly: Boolean)
}
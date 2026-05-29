package com.example.news.domain.usecase

import com.example.news.domain.entity.Language
import com.example.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateWifiOnlyUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(wifiOnly: Boolean) =
        settingsRepository.updateWifiOnly(wifiOnly)
}
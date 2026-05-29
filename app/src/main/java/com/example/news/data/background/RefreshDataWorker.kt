package com.example.news.data.background

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.news.domain.usecase.GetSettingsUseCase
import com.example.news.domain.usecase.UpdateArticlesForAllSubscriptionsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class RefreshDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val updateArticlesForAllSubscriptionsUseCase: UpdateArticlesForAllSubscriptionsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase,
    private val notificationsHelper: NotificationsHelper
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        Log.d("RefreshDataWorker", "start worker")
        val settings = getSettingsUseCase().first()
        val updatedTopics = updateArticlesForAllSubscriptionsUseCase()
        if (updatedTopics.isNotEmpty() && settings.notificationEnabled) {
            notificationsHelper.showNewArticlesNotification(updatedTopics)
        }
        Log.d("RefreshDataWorker", "stop worker")
        return Result.success()
    }
}
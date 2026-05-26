package com.example.news.data.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.news.domain.usecase.UpdateArticlesForAllSubscriptionsUseCase

class RefreshDataWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val updateArticlesForAllSubscriptionsUseCase: UpdateArticlesForAllSubscriptionsUseCase
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        updateArticlesForAllSubscriptionsUseCase()
        return Result.success()
    }
}